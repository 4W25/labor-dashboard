package com.tsmc.labor_manpower_api.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDatasetDto {
    private String label; // e.g., "ME", "IE" for job distribution, or "20-29" for age
    private List<? extends Number> data; // List of values for this dataset
    private List<String> backgroundColor; // Optional: if colors are set server-side
    // Add other chart.js dataset properties if needed: borderColor, fill, etc.
}
