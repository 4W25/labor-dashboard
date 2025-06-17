package com.tsmc.labor_manpower_api.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDto {
    private List<String> labels; // Labels for the x-axis or pie chart segments
    private List<ChartDatasetDto> datasets;
}
