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
import com.uit.realestate.payload.scraper.RawDataScraper;
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
public class ScraperPropzyServiceImpl extends ScraperService {

    private final String urlBase;
    private final String BED_ROOM_STRING = "Ph??ng ng???";
    private final String BATH_ROOM_STRING = "Ph??ng t???m";
    private final String AREA_STRING = "Di????n t??ch";
    private final String DIRECTION_STRING = "H?????ng";

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
        int pageNumberBuy = 1;
        int pageNumberRent = 1;
        int quantity = 0;
        do {
            StringBuilder urlExtends = new StringBuilder();
            if (pageNumberBuy > pageNumberRent) {
                urlExtends.append("/thue/bat-dong-san/hcm/p").append(pageNumberRent);
                pageNumberRent++;
            } else {
                urlExtends.append("/mua/bat-dong-san/hcm/p").append(pageNumberBuy);
                pageNumberBuy++;
            }
            String urlWithPage = urlBase + urlExtends;
            for (String url : extractLink(urlWithPage)) {
                boolean rs = extractAndSaveDataFromDetailPage(url, null);
                if (rs) {
                    quantity++;
                }
                if (quantity >= size) {
                    break;
                }
            }
        } while (quantity < size);
    }

    @Override
    protected List<String> extractLink(String url) {
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

    @Override
    protected List<RawDataScraper> extractRawData(String url, ETypeApartment type) {
        throw new InvalidException("Not Support");
    }

    @Override
    @Transactional
    protected boolean extractAndSaveDataFromDetailPage(String url, RawDataScraper rawData) {
        // do not use rawData
        LogScrapingRequest logScrapingRequest = null;
        String title = null;
        MultipartFile[] filesTemp = new MultipartFile[0];
        Set<FileCaption> photos = new HashSet<>();
        AddApartmentRequest addApartmentRequest = new AddApartmentRequest();
        addApartmentRequest.setStatus(EApartmentStatus.OPEN);
        addApartmentRequest.setAuthorId(AppConstant.ADMIN_ID_ACCOUNT);
        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url).get();
            title = getValueSingle("Title", document, ".t-detail", "h1", null, true);
            if (apartmentService.existApartment(title)) {
                return false;
            }
            addApartmentRequest.setTitle(title);
            //Get images
            List<String> linkImgList = getValue("Image", document, ".project-detail-feature.rtux", "img", "src", true);
            filesTemp = FileHandler.downloadFileFromUrls(linkImgList);
            //Get typeApartment;
            String typeApartment = getValueSingle("Type Apartment", document, ".t-detail", ".label-1", null, true);
            addApartmentRequest.setTypeApartment(ETypeApartment.of(typeApartment));
            //Get totalPrice;
            String totalPrice = getValueSingle("Total Price", document, ".t-detail", ".p-price-n", null, true);
            if (addApartmentRequest.getTypeApartment().equals(ETypeApartment.BUY)){
                addApartmentRequest.setTotalPrice(StringUtils.castNumberFromStringPrice(totalPrice));
            }else{
                addApartmentRequest.setPriceRent(StringUtils.castNumberFromStringPrice(totalPrice));
                addApartmentRequest.setUnitRent(totalPrice.trim());
            }
            //Get categoryId;
            String link = url.replaceAll(EScraper.PROPZY.getValue(), "");
            List<String> splits = Arrays.asList(link.split("/", 5));
            link = "";
            for (int i = 1; i < splits.size() - 1; i++) {
                link = link.concat("/").concat(splits.get(i));
            }
            String category = getValueSingle("Category", document, ".fixe", "a[href=" + link + "]", null, true);
            addApartmentRequest.setCategoryId(categoryService.findOrCreate(category).getId());
            // Detail Apartment
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
            addApartmentRequest.setArea(area);
            double bedRoom = StringUtils.castNumberFromString(detailMap.get(BED_ROOM_STRING));
            double bathRoom = StringUtils.castNumberFromString(detailMap.get(BATH_ROOM_STRING));
            int floor = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            int toilet = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            String direction = detailMap.get(DIRECTION_STRING);
            String address = getValueSingle("Address", document, ".t-detail", ".p-address", null, true);
            String description = getHtml(document, "#tab-overview", ".tab-content.entry-content");
            addApartmentRequest.setApartmentDetail(ApartmentDetailRequest
                    .builder()
                    .description(description)
                    .houseDirection(direction)
                    .floorQuantity(floor)
                    .bedroomQuantity((int) bedRoom)
                    .bathroomQuantity((int) bathRoom)
                    .toiletQuantity(toilet)
                    .moreInfo(moreInfo)
                    .build());
            // Address
            DistrictDto district = districtService.findDistrictNameIn(address);
            ProvinceDto province = provinceService.findByDistrict(district.getId());
            addApartmentRequest.setApartmentAddress(ApartmentAddressRequest.builder()
                    .address(address)
                    .provinceId(province.getId())
                    .districtId(district.getId())
                    .countryCode(AppConstant.DEFAULT_COUNTRY)
                    .build());
            // Photos
            photos = uploadService.uploadPhoto(filesTemp, APARTMENT_FILE, AppConstant.ADMIN_ID_ACCOUNT);
            addApartmentRequest.setPhotos(new ArrayList<>(photos));
            boolean result = apartmentService.addApartment(addApartmentRequest);
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
}
