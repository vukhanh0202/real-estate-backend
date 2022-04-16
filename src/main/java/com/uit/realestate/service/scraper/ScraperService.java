package com.uit.realestate.service.scraper;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.payload.scraper.RawDataScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ScraperService {

    @Async
    public abstract void scrapingData(int size);

    protected abstract List<String> extractLink(String url);

    protected abstract List<RawDataScraper> extractRawData(String url, ETypeApartment type);

    @Transactional
    protected abstract boolean extractAndSaveDataFromDetailPage(String url, RawDataScraper rawData);

    protected String getHtml(Document document, String queryWrapper, String query) {
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

    protected List<String> getValue(String value, Document document, String queryWrapper, String query, String attr, boolean isRequired) {
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

    protected String getValueSingle(String value, Document document, String queryWrapper, String query, String attr, boolean isRequired) {
        List<String> list = getValue(value, document, queryWrapper, query, attr, isRequired);
        if (list == null) {
            return null;
        }
        return list.stream().findAny().orElse(null);
    }

    protected String getValueFromTextNode(Element element) {
        if (element == null){
            return null;
        }
        return ((TextNode) Objects.requireNonNull((element.childNodes().stream().filter(c -> {
            if (!(c instanceof TextNode)) {
                return false;
            }
            return !Objects.equals(((TextNode) c).text(), " ");
        }).findFirst()).orElse(null))).text();
    }
}