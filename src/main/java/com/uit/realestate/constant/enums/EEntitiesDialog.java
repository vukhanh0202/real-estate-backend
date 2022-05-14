package com.uit.realestate.constant.enums;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.utils.HandleDialogFlowWithDoubleListUtils;
import com.uit.realestate.utils.HandleDialogFlowWithIDListUtils;
import com.uit.realestate.utils.HandleDialogFlowWithSingleStringUtils;
import com.uit.realestate.utils.HandleDialogFlowWithStringListUtils;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

public enum EEntitiesDialog {
    AREA("area", HandleDialogFlowWithDoubleListUtils.class),
    BATHROOMS("bathrooms", HandleDialogFlowWithDoubleListUtils.class),
    BEDROOMS("bedRooms", HandleDialogFlowWithDoubleListUtils.class),
    CATEGORY("category", HandleDialogFlowWithIDListUtils.class),
    DIRECTION("direction", HandleDialogFlowWithStringListUtils.class),
    DISTRICT("district", HandleDialogFlowWithIDListUtils.class),
    FLOORS("floors", HandleDialogFlowWithDoubleListUtils.class),
    PRICE("price", HandleDialogFlowWithDoubleListUtils.class),
    PROVINCE("province", HandleDialogFlowWithIDListUtils.class),
    TYPE_APARTMENT("typeapartment", HandleDialogFlowWithSingleStringUtils.class);

    @Getter
    private final String value;

    @Getter
    private final Class<?> clazz;

    EEntitiesDialog(String value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;

    }

    public static EEntitiesDialog of(String str) {
        for (EEntitiesDialog item : values()) {
            if (str.toLowerCase().equals(item.getValue())) {
                return item;
            }
        }
        return null;
    }

    public static void setValueIntoApartmentQueryParam(ApartmentQueryParam param, String key, String value) {
        EEntitiesDialog entity = EEntitiesDialog.of(key);
        if (Objects.isNull(entity)) {
            return;
        }
        try {
            switch (entity) {
                case AREA:
                    List<Double> areas = EEntitiesDialog.castStringToList(entity.getClazz(), Double.class, value);
                    if (areas == null || areas.isEmpty()) {
                        return;
                    }
                    if (areas.size() == 1) {
                        Double val = areas.get(0);
                        param.setAreaLow(val - (val * AppConstant.PERCENT_RANGE));
                        param.setAreaHigh(val + (val * AppConstant.PERCENT_RANGE));
                    } else {
                        param.setAreaLow(areas.get(0));
                        param.setAreaHigh(areas.get(1));
                    }
                    break;
                case BATHROOMS:
                    List<Integer> bathRooms = EEntitiesDialog.castStringToList(entity.getClazz(), Integer.class, value);
                    if (bathRooms == null || bathRooms.isEmpty()) {
                        return;
                    }
                    param.setBathrooms(bathRooms);
                    break;
                case BEDROOMS:
                    List<Integer> bedRooms = EEntitiesDialog.castStringToList(entity.getClazz(), Integer.class, value);
                    if (bedRooms == null || bedRooms.isEmpty()) {
                        return;
                    }
                    param.setBedrooms(bedRooms);
                    break;
                case CATEGORY:
                    List<Long> categories = EEntitiesDialog.castStringToList(entity.getClazz(), Long.class, value);
                    if (categories == null || categories.isEmpty()) {
                        return;
                    }
                    param.setCategories(categories);
                    break;
                case DIRECTION:
                    List<String> directions = EEntitiesDialog.castStringToList(entity.getClazz(), String.class, value);
                    if (directions == null || directions.isEmpty()) {
                        return;
                    }
                    param.setDirections(directions);
                    break;
                case DISTRICT:
                    List<Long> districts = EEntitiesDialog.castStringToList(entity.getClazz(), Long.class, value);
                    if (districts == null || districts.isEmpty()) {
                        return;
                    }
                    param.setDistricts(districts);
                    break;
                case FLOORS:
                    List<Integer> floors = EEntitiesDialog.castStringToList(entity.getClazz(), Integer.class, value);
                    if (floors == null || floors.isEmpty()) {
                        return;
                    }
                    param.setFloors(floors);
                    break;
                case PRICE:
                    List<Double> prices = EEntitiesDialog.castStringToList(entity.getClazz(), Double.class, value);
                    if (prices == null || prices.isEmpty()) {
                        return;
                    }
                    if (prices.size() == 1) {
                        Double val = prices.get(0);
                        param.setPriceLow(val - (val * AppConstant.PERCENT_RANGE));
                        param.setPriceHigh(val + (val * AppConstant.PERCENT_RANGE));
                    } else {
                        param.setPriceLow(prices.get(0));
                        param.setPriceHigh(prices.get(1));
                    }
                    break;
                case PROVINCE:
                    List<Long> provinces = EEntitiesDialog.castStringToList(entity.getClazz(), Long.class, value);
                    if (provinces == null || provinces.isEmpty()) {
                        return;
                    }
                    param.setProvinces(provinces);
                    break;
                case TYPE_APARTMENT:
                    List<String> types = EEntitiesDialog.castStringToList(entity.getClazz(), String.class, value);
                    if (types == null || types.isEmpty()) {
                        return;
                    }
                    param.setType(types.get(0).toUpperCase());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T, R> List<R> castStringToList(Class<T> clazz, Class<R> output, String value) {
        try {
            if (clazz == HandleDialogFlowWithDoubleListUtils.class) {
                return (List<R>) HandleDialogFlowWithDoubleListUtils.execute(value);
            } else if (clazz == HandleDialogFlowWithIDListUtils.class) {
                return (List<R>) HandleDialogFlowWithIDListUtils.execute(value);
            } else if (clazz == HandleDialogFlowWithSingleStringUtils.class) {
                return (List<R>) HandleDialogFlowWithSingleStringUtils.execute(value);
            } else if (clazz == HandleDialogFlowWithStringListUtils.class) {
                return (List<R>) HandleDialogFlowWithStringListUtils.execute(value);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
