package com.uit.realestate.service.scraper.impl;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.ApartmentDetailRequest;
import com.uit.realestate.payload.apartment.LogScrapingRequest;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.apartment.LogScrapingService;
import com.uit.realestate.service.category.CategoryService;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.service.scraper.ScraperService;
import com.uit.realestate.utils.FileHandler;
import com.uit.realestate.utils.NumberUtils;
import com.uit.realestate.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.uit.realestate.constant.AppConstant.APARTMENT_FILE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ScraperPropzyServiceImpl implements ScraperService {

    private final String urlBase;
    private final String BED_ROOM_STRING = "Phòng ngủ";
    private final String BATH_ROOM_STRING = "Phòng tắm";
    private final String AREA_STRING = "Diện tích";
    private final String DIRECTION_STRING = "Hướng";

    private final ApartmentService apartmentService;

    private final DistrictService districtService;

    private final ProvinceService provinceService;

    private final CategoryService categoryService;

    private final UploadService uploadService;

    private final LogScrapingService logScrapingService;

    private final ApplicationProperties app;

    public ScraperPropzyServiceImpl() {
        this.urlBase = null;
        this.apartmentService = null;
        this.districtService = null;
        this.categoryService = null;
        this.uploadService = null;
        this.logScrapingService = null;
        this.provinceService = null;
        this.app = null;
    }

    public ScraperPropzyServiceImpl(String url, ApartmentService apartmentService, DistrictService districtService, ProvinceService provinceService, CategoryService categoryService, UploadService uploadService, LogScrapingService logScrapingService, ApplicationProperties app) {
        this.urlBase = url;
        this.apartmentService = apartmentService;
        this.districtService = districtService;
        this.provinceService = provinceService;
        this.categoryService = categoryService;
        this.uploadService = uploadService;
        this.logScrapingService = logScrapingService;
        this.app = app;
    }

    @Override
    @Async
    public void scrapingData(int size) {
        int pageNumber = 1;
        int quantity = 0;
        do {
            String urlWithPage = urlBase + "/p" + pageNumber;
            for (String url : extractLink(urlWithPage)) {
                boolean rs = saveDataFromDetailPage(url);
                if (rs) {
                    quantity++;
                }
                if (quantity >= size) {
                    break;
                }
            }
            pageNumber++;
            if (quantity >= size) {
                break;
            }
        } while (true);
    }

    private List<String> extractLink(String url) {
        List<String> result = new ArrayList<>();
        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url)
                    .get();

            //Selecting the element which contains the ad list
            Element element = document.getElementById("view-as-grid");
            //getting all the <a> tag elements inside the content div tag
            List<Element> elements = element.getElementsByTag("a")
                    .stream()
                    .filter(ele -> !ele.getElementsByTag("h3").isEmpty())
                    .collect(Collectors.toList());
            //traversing through the elements
            for (Element ads : elements) {
                String link = ads.attr("href");
                if (Objects.nonNull(link)) result.add(EScraper.PROPZY.getValue() + link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    private boolean saveDataFromDetailPage(String url) {
        LogScrapingRequest logScrapingRequest = null;
        String title = null;
        MultipartFile[] filesTemp = new MultipartFile[0];
        Set<FileCaption> photos = new HashSet<>();
        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url).get();
            title = getValueSingle("Title", document, ".t-detail", "h1", null, true);
            if (apartmentService.existApartment(title)) {
                return false;
            }
            //Get images
            List<String> linkImgList = getValue("Image", document, ".project-detail-feature.rtux", "img", "src", false);
            filesTemp = FileHandler.downloadFileFromUrls(linkImgList);
            //Get totalPrice;
            String totalPrice = getValueSingle("Total Price", document, ".t-detail", ".p-price-n", null, true);
            //Get typeApartment;
            String typeApartment = getValueSingle("Type Apartment", document, ".t-detail", ".label-1", null, true);
            //Get categoryId;
            String link = url.replaceAll(EScraper.PROPZY.getValue(), "");
            List<String> splits = Arrays.asList(link.split("/", 5));
            link = "";
            for (int i = 1; i < splits.size() - 1; i++) {
                link = link.concat("/").concat(splits.get(i));
            }
            String category = getValueSingle("Category", document, ".fixe", "a[href=" + link + "]", null, true);
            List<String> moreInfo = getValue("More info", document, "#tab-utilities .tab-content", "span", null, false);
            Element detailElement = document.getElementsByAttributeValue("class", "bl-parameter-listing").stream().findFirst().get();
            Map<String, String> detailMap = new HashMap<>();
            detailElement.getElementsByTag("li")
                    .forEach(item -> {
                        try {
                            Elements elements = item.getElementsByTag("span");
                            String key = "";
                            String value = "";
                            for (Element element : elements) {
                                if (Objects.equals(element.attr("class"), "sp-text")) {
                                    key = getValueFromTextNode(element);
                                } else {
                                    value = getValueFromTextNode(element);
                                }
                            }
                            detailMap.put(key, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            double area = StringUtils.castNumberFromString(detailMap.get(AREA_STRING));
            double bedRoom = StringUtils.castNumberFromString(detailMap.get(BED_ROOM_STRING));
            double bathRoom = StringUtils.castNumberFromString(detailMap.get(BATH_ROOM_STRING));
            int floor = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            int toilet = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            String direction = detailMap.get(DIRECTION_STRING);
            String address = getValueSingle("Address", document, ".t-detail", ".p-address", null, true);
            String description = getHtml(document, "#tab-overview", ".tab-content.entry-content");
            DistrictDto district = getDistrictFromString(address);
            ProvinceDto province = getProvinceFromDistrict(district.getId());
            photos = uploadService.uploadPhoto(filesTemp, APARTMENT_FILE, AppConstant.ADMIN_ID_ACCOUNT);
            boolean result = apartmentService.addApartment(AddApartmentRequest
                    .builder()
                    .title(title)
                    .area(area)
                    .status(EApartmentStatus.OPEN)
                    .totalPrice(StringUtils.castNumberFromStringPriceBillion(totalPrice))
                    .typeApartment(ETypeApartment.of(typeApartment))
                    .apartmentAddress(ApartmentAddressRequest.builder()
                            .address(address)
                            .provinceId(province.getId())
                            .districtId(district.getId())
                            .countryCode(AppConstant.DEFAULT_COUNTRY)
                            .build())
                    .categoryId(categoryService.findOrCreate(category).getId())
                    .apartmentDetail(ApartmentDetailRequest
                            .builder()
                            .description(description)
                            .houseDirection(direction)
                            .floorQuantity(floor)
                            .bedroomQuantity((int) bedRoom)
                            .bathroomQuantity((int) bathRoom)
                            .toiletQuantity(toilet)
                            .moreInfo(moreInfo)
                            .build())
                    .photos(new ArrayList<>(photos))
                    .authorId(AppConstant.ADMIN_ID_ACCOUNT)
                    .build());
            if (result) {
                logScrapingRequest = LogScrapingRequest.builder()
                        .status(true)
                        .idApartment(apartmentService.findByTitleNewest(title))
                        .linkScraping(url)
                        .titleApartment(title)
                        .build();
            } else {
                logScrapingRequest = LogScrapingRequest.builder()
                        .status(false)
                        .linkScraping(url)
                        .error("Create Apartment Fail!")
                        .build();
            }
            return result;
        } catch (Exception e) {
            logScrapingRequest = LogScrapingRequest.builder()
                    .status(false)
                    .linkScraping(url)
                    .error(e.getMessage())
                    .build();
            if (Objects.nonNull(title)) {
                apartmentService.deletePermanent(title);
            }
            photos.forEach(fileCaption -> {
                try {
                    File file = FileUtils.getFile(fileCaption.getOriginalName());
                    Files.copy(file.toPath(), Paths.get(app.getSource().getDirectory() + "/deleteFiles/" + fileCaption.getName()), REPLACE_EXISTING);
                    file.delete();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            return false;
        } finally {
            if (Objects.nonNull(logScrapingRequest)) {
                logScrapingService.save(logScrapingRequest);
                Arrays.stream(filesTemp).forEach(multipartFile -> {
                    try {
                        File file = FileUtils.getFile(multipartFile.getOriginalFilename());
                        Files.copy(file.toPath(), Paths.get(app.getSource().getDirectory() + "/deleteFiles/" + multipartFile.getOriginalFilename()), REPLACE_EXISTING);
                        file.deleteOnExit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private String getHtml(Document document, String queryWrapper, String query) {
        Element elementWrap = document.select(queryWrapper)
                .stream().findFirst().orElse(null);
        if (elementWrap == null) {
            return "";
        } else {
            Elements elementListWrap = elementWrap.select(query);
            StringBuilder result = new StringBuilder();
            elementListWrap
                    .forEach(rs -> result.append(rs.html()));

            return result.toString();
        }
    }

    private List<String> getValue(String value, Document document, String queryWrapper, String query, String attr, boolean isRequired) {
        Element elementWrap = document.select(queryWrapper)
                .stream().findFirst().orElse(null);
        if (elementWrap == null) {
            if (isRequired) {
                throw new InvalidException(value + " must not be null");
            }
            return Collections.emptyList();
        } else {
            Elements elementListWrap = elementWrap.select(query);

            List<String> resultList = elementListWrap
                    .stream()
                    .map(rs -> {
                        if (attr == null) {
                            return getValueFromTextNode(rs);
                        }
                        return rs.attr(attr);
                    })
                    .collect(Collectors.toList());
            if (resultList.isEmpty() && isRequired) {
                return null;
            }
            return resultList;
        }
    }

    private String getValueSingle(String value, Document document, String queryWrapper, String query, String attr, boolean isRequired) {
        List<String> list = getValue(value, document, queryWrapper, query, attr, isRequired);
        if (list == null) {
            return null;
        }
        return list.stream().findAny().orElse(null);
    }

    private String getValueFromTextNode(Element element) {
        return ((TextNode) Objects.requireNonNull((element.childNodes().stream().filter(c -> {
            if (!(c instanceof TextNode)) {
                return false;
            }
            return !Objects.equals(((TextNode) c).text(), " ");
        }).findFirst()).orElse(null))).text();
    }

    private DistrictDto getDistrictFromString(String str) {
        return districtService.findDistrictNameIn(str);
    }

    private ProvinceDto getProvinceFromDistrict(Long districtId) {
        return provinceService.findByDistrict(districtId);
    }

}
