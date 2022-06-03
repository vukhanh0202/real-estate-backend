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
public class ScraperAloNhaDatImpl extends ScraperService {

    private final String urlBase;

    private final ApartmentService apartmentService;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final CategoryService categoryService;
    private final UploadService uploadService;
    private final LogScrapingService logScrapingService;
    private final ApplicationProperties app;

    public ScraperAloNhaDatImpl() {
        this.apartmentService = null;
        this.districtService = null;
        this.provinceService = null;
        this.categoryService = null;
        this.uploadService = null;
        this.logScrapingService = null;
        this.app = null;
        this.urlBase = null;
    }

    public ScraperAloNhaDatImpl(String urlBase, ApartmentService apartmentService, DistrictService districtService, ProvinceService provinceService, CategoryService categoryService, UploadService uploadService, LogScrapingService logScrapingService, ApplicationProperties app) {
        this.urlBase = urlBase;
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
        //Page 2655
        int pageNumberBuy = 1;
        int pageNumberRent = 1;
        int quantity = 0;
        ETypeApartment type;
        do {
            StringBuilder urlExtends = new StringBuilder();
            if (pageNumberBuy > pageNumberRent) {
                urlExtends.append("/cho-thue/trang--").append(pageNumberRent).append(".html");
                pageNumberRent++;
                type = ETypeApartment.RENT;
            } else {
                urlExtends.append("/can-ban/trang--").append(pageNumberBuy).append(".html");
                pageNumberBuy++;
                type = ETypeApartment.BUY;
            }
            String urlWithPage = urlBase + urlExtends;
            for (RawDataScraper rawData : extractRawData(urlWithPage, type)) {
                boolean rs = extractAndSaveDataFromDetailPage(rawData.getLink(), rawData);
                if (rs) {
                    quantity++;
                }
                if (quantity >= size) {
                    break;
                }
            }
            System.out.println("Current: " + quantity);
            System.out.println("Page: " + pageNumberBuy);
        } while (quantity < size);
        System.out.println("Finish Scraping");
    }

    @Override
    protected List<String> extractLink(String url) {
        throw new InvalidException("Not Support");
    }

    @Override
    protected List<RawDataScraper> extractRawData(String url, ETypeApartment type) {
        //loading the HTML to a Document Object
        try {
            Document document = Jsoup.connect(url).get();
            List<RawDataScraper> result = new ArrayList<>();
            Elements elementWraps = document.select("#left .content-items .content-item");

            //traversing through the elements
            for (Element ele : elementWraps) {
                String direc = getValueFromTextNode(ele.select(".text .square-direct .ct_direct").stream().findFirst().orElse(null));
                result.add(RawDataScraper.builder()
                        .type(type)
                        .link(EScraper.ALOND.getValue() + ele.select(".thumbnail a").attr("href"))
                        .title(getValueFromTextNode(ele.select(".ct_title a").stream().findFirst().orElse(null)))
                        .floors(getValueFromTextNode(ele.select(".text .characteristics .floors").stream().findFirst().orElse(null)))
                        .bedrooms(getValueFromTextNode(ele.select(".text .characteristics .bedroom").stream().findFirst().orElse(null)))
                        .areas(getValueFromTextNode(ele.select(".text .square-direct .ct_dt").stream().findFirst().orElse(null)))
                        .direction(direc.contains("_") ? null : direc)
                        .price(getValueFromTextNode(ele.select(".text .price-dis .ct_price").stream().findFirst().orElse(null)))
                        .address(getValueFromTextNode(ele.select(".text .price-dis .ct_dis a").get(0))
                                + ", " + getValueFromTextNode(ele.select(".text .price-dis .ct_dis a").get(1)))
                        .district(getValueFromTextNode(ele.select(".text .price-dis .ct_dis a").get(2)))
                        .province(getValueFromTextNode(ele.select(".text .price-dis .ct_dis a").get(3)))
                        .build());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    protected boolean extractAndSaveDataFromDetailPage(String url, RawDataScraper rawData) {
        LogScrapingRequest logScrapingRequest = null;
        String title = null;
        MultipartFile[] filesTemp = new MultipartFile[0];
        Set<FileCaption> photos = new HashSet<>();
        AddApartmentRequest addApartmentRequest = new AddApartmentRequest();
        addApartmentRequest.setStatus(EApartmentStatus.OPEN);
        addApartmentRequest.setAuthorId(AppConstant.ADMIN_ID_ACCOUNT);
        // Address
        DistrictDto district;
        ProvinceDto province;
        try {

            district = districtService.findDistrictNameIn(rawData.getDistrict().toLowerCase().replace("thành phố", "").trim());
            province = provinceService.findByDistrict(district.getId());
            if (province.getId() == 1) {
                System.out.println("Skip HCM apartment");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url).get();
            title = rawData.getTitle();
            if (apartmentService.existApartment(title)) {
                return false;
            }
            addApartmentRequest.setTitle(title);
            //Get images
            List<String> linkImgList = getValue("Image", document, "#left .property .images", "img", "src", true);
            linkImgList = linkImgList.size() > AppConstant.MAX_IMAGES_APARTMENT ? linkImgList.subList(0, 8) : linkImgList;
            filesTemp = FileHandler.downloadFileFromUrls(linkImgList.stream()
                    .filter(item -> !item.contains(EScraper.ALOND.getValue())).map(item -> EScraper.ALOND.getValue() + item)
                    .collect(Collectors.toList()));
            //Get typeApartment;
            addApartmentRequest.setTypeApartment(rawData.getType());
            //Get totalPrice;
            String totalPrice = rawData.getPrice();
            if (addApartmentRequest.getTypeApartment().equals(ETypeApartment.BUY)) {
                addApartmentRequest.setTotalPrice(StringUtils.castNumberFromStringPrice(totalPrice));
            } else {
                addApartmentRequest.setPriceRent(StringUtils.castNumberFromStringPrice(totalPrice));
                addApartmentRequest.setUnitRent(totalPrice.trim());
            }
            //Get categoryId;
            String category = getValueSingle("Category", document, "#right .search-box .property-type-container.multicolumns", ".text", null, true);
            addApartmentRequest.setCategoryId(categoryService.findOrCreate(category).getId());
            //Area
            addApartmentRequest.setArea(StringUtils.castNumberFromString(rawData.getAreas()));
            // Detail
            double bedRoom = StringUtils.castNumberFromString(rawData.getBedrooms());
            double floor = StringUtils.castNumberFromString(rawData.getFloors());
            int bathRoom = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            int toilet = (int) (bedRoom + NumberUtils.random(-bedRoom + 1, bedRoom - 1));
            String description = getHtml(document, "#left", ".detail ");
            if (org.apache.commons.lang3.StringUtils.isBlank(description)){
                throw new InvalidException("not found description");
            }
            addApartmentRequest.setApartmentDetail(ApartmentDetailRequest
                    .builder()
                    .description(description)
                    .houseDirection(rawData.getDirection())
                    .floorQuantity((int) floor)
                    .bedroomQuantity((int) bedRoom)
                    .bathroomQuantity(bathRoom)
                    .toiletQuantity(toilet)
                    .moreInfo(null)
                    .build());
            // Address
//            DistrictDto district = districtService.findDistrictNameIn(rawData.getDistrict());
//            ProvinceDto province = provinceService.findByDistrict(district.getId());
            addApartmentRequest.setApartmentAddress(ApartmentAddressRequest.builder()
                    .address(rawData.getAddress())
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