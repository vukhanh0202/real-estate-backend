package com.uit.realestate.service.scraper.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.scraper.LinkDto;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.ApartmentDetailRequest;
import com.uit.realestate.service.scraper.ScraperService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScraperPropzyServiceImpl implements ScraperService {

    private final String urlBase;
    private final String BED_ROOM_STRING = "Phòng ngủ";
    private final String BATH_ROOM_STRING = "Phòng tắm";
    private final String AREA_STRING = "Diện tích";
    private final String DIRECTION_STRING = "Hướng";

    public ScraperPropzyServiceImpl() {
        this.urlBase = null;
    }

    public ScraperPropzyServiceImpl(String url) {
        this.urlBase = url;
    }

    @Override
    public List<LinkDto> scrapingData(int size) {
        int pageNumber = 1;
        for (String url : extractLink(urlBase)) {
            saveDataFromDetailPage(url);
        }
        return Collections.EMPTY_LIST;
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

    private boolean saveDataFromDetailPage(String url) {
        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url)
                    .get();
            // TODO: Validation
            //Get images
            List<String> linkImgList = getValue(document, "project-detail-feature rtux", "img", "src", false);
            //Get title
            String title = getValueSingle(document, "t-detail", "h1", null, true);

            //Get status = pending

            //Get totalPrice;
            String totalPrice = getValueSingle(document, "t-detail", ".p-price-n", null, true);
            //Get typeApartment;
            String typeApartment = getValueSingle(document, "t-detail", ".label-1", null, true);
            //Get categoryId;
            String link = url.replaceAll(EScraper.PROPZY.getValue(), "");
            List<String> splits = Arrays.asList(link.split("/", 5));
            link = "";
            for (int i = 1; i < splits.size() - 1; i++) {
                link = link.concat("/").concat(splits.get(i));
            }
            String category = getValueSingle(document, "fixe", "a[href=" + link + "]", null, true);
            //Get apartmentDetail;
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
            //Get area;
            String area = detailMap.get(AREA_STRING);
            String bedRoom = detailMap.get(BED_ROOM_STRING);
            String bathRoom = detailMap.get(BATH_ROOM_STRING);
            String direction = detailMap.get(DIRECTION_STRING);
            //elementWrap.select("a[href=/mua/can-ho-officetel/hcm]")
            // address
            String address = getValueSingle(document, "t-detail", ".p-address", null, true);
            //Get photos;
            //Get authorId;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<String> getValue(Document document, String classWrapper, String query, String attr, boolean isRequired) {
        Element elementWrap = document.getElementsByAttributeValue("class", classWrapper)
                .stream().findFirst().orElse(null);
        if (elementWrap == null) {
            if (isRequired) {
                return null;
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

    private String getValueSingle(Document document, String classWrapper, String query, String attr, boolean isRequired) {
        List<String> list = getValue(document, classWrapper, query, attr, isRequired);
        if (list == null) {
            return null;
        }
        return list.stream().findAny().orElse(null);
    }

    private String getValueFromTextNode(Element element) {
        return ((TextNode) (element.childNodes().stream().filter(c -> {
            if (!(c instanceof TextNode)) {
                return false;
            }
            return !Objects.equals(((TextNode) c).text(), " ");
        }).findFirst()).orElse(null)).text();
    }
}
