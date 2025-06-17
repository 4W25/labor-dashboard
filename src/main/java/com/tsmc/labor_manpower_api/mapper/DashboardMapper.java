package com.tsmc.labor_manpower_api.mapper;

import com.tsmc.labor_manpower_api.dto.DashboardStatsDto;
import com.tsmc.labor_manpower_api.dto.chart.ChartDataDto;
import com.tsmc.labor_manpower_api.dto.chart.ChartDatasetDto;
import com.tsmc.labor_manpower_api.dto.CompanyGraphDataDto;
import com.tsmc.labor_manpower_api.dto.CompanyRelationshipLinkDto;
import com.tsmc.labor_manpower_api.dto.CompanyRelationshipNodeDto;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardMapper {

    public static DashboardStatsDto toDashboardStatsDto(long totalPersonnel, long mainContractors, long subContractors, long totalCompanies) {
        return new DashboardStatsDto(totalPersonnel, mainContractors, subContractors, totalCompanies);
    }

    @SuppressWarnings("unchecked")
    public static ChartDataDto toChartDataDto(Map<String, Object> serviceLayerMap) {
        if (serviceLayerMap == null || !serviceLayerMap.containsKey("labels") || !serviceLayerMap.containsKey("values")) {
            // Or throw an error, or return an empty ChartDataDto
            return new ChartDataDto(List.of(), List.of());
        }

        List<String> labels = (List<String>) serviceLayerMap.get("labels");

        // Handle two types of "values" structures from service:
        // 1. Simple list of values (for pie/bar chart with one dataset)
        // 2. List of dataset maps (for line/bar chart with multiple datasets)

        Object valuesObject = serviceLayerMap.get("values");
        List<ChartDatasetDto> datasets;

        if (valuesObject instanceof List && !((List<?>) valuesObject).isEmpty() && ((List<?>) valuesObject).get(0) instanceof Map) {
            // Case 2: List of dataset maps
             List<Map<String, Object>> datasetMaps = (List<Map<String, Object>>) serviceLayerMap.get("datasets"); // If key is "datasets"
             if (datasetMaps == null) datasetMaps = (List<Map<String, Object>>) valuesObject; // if key is "values" but contains datasets

            datasets = datasetMaps.stream().map(datasetMap -> {
                String label = (String) datasetMap.get("label");
                List<? extends Number> data = (List<? extends Number>) datasetMap.get("data");
                // Potentially add backgroundColor or other properties if present in map
                return new ChartDatasetDto(label, data, null);
            }).collect(Collectors.toList());
        } else if (valuesObject instanceof List) {
            // Case 1: Simple list of values, wrap into a single dataset
            List<? extends Number> values = (List<? extends Number>) valuesObject;
            ChartDatasetDto singleDataset = new ChartDatasetDto("Dataset", values, null); // Default label
             // Try to get a label from the map if available, e.g. serviceLayerMap.get("datasetLabel")
            if (serviceLayerMap.containsKey("datasetLabel")) {
                singleDataset.setLabel((String) serviceLayerMap.get("datasetLabel"));
            }
            datasets = List.of(singleDataset);
        } else {
            // Invalid structure
            datasets = List.of();
        }

        return new ChartDataDto(labels, datasets);
    }

    @SuppressWarnings("unchecked")
    public static CompanyGraphDataDto toCompanyGraphDataDto(Map<String, Object> serviceLayerMap) {
        if (serviceLayerMap == null) {
            return new CompanyGraphDataDto(List.of(), List.of());
        }

        List<Map<String, Object>> nodesMapList = (List<Map<String, Object>>) serviceLayerMap.getOrDefault("nodes", List.of());
        List<Map<String, String>> linksMapList = (List<Map<String, String>>) serviceLayerMap.getOrDefault("links", List.of());

        List<CompanyRelationshipNodeDto> nodes = nodesMapList.stream()
            .map(nodeMap -> new CompanyRelationshipNodeDto(
                (String) nodeMap.get("id"),
                (Number) nodeMap.get("size"),
                (String) nodeMap.get("group")
            ))
            .collect(Collectors.toList());

        List<CompanyRelationshipLinkDto> links = linksMapList.stream()
            .map(linkMap -> new CompanyRelationshipLinkDto(
                linkMap.get("source"),
                linkMap.get("target")
            ))
            .collect(Collectors.toList());

        return new CompanyGraphDataDto(nodes, links);
    }

     public static ChartDataDto toChartDataDtoFromService(Map<String, Object> serviceLayerMap) {
        List<String> labels = (List<String>) serviceLayerMap.get("labels");
        List<Map<String, Object>> datasetMaps = (List<Map<String, Object>>) serviceLayerMap.get("datasets");

        List<ChartDatasetDto> datasets = datasetMaps.stream().map(datasetMap -> {
            String label = (String) datasetMap.get("label");
            List<? extends Number> data = (List<? extends Number>) datasetMap.get("data");
            // You could also extract 'backgroundColor' or other properties if your service provides them
            return new ChartDatasetDto(label, data, null); // Assuming no specific background colors from service for now
        }).collect(Collectors.toList());

        return new ChartDataDto(labels, datasets);
    }
}
