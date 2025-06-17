from flask import Flask, jsonify, request
from google.cloud import bigquery
from schema import (
    users_schema,
    personnel_schema,
    companies_schema,
    projects_schema,
    company_projects_schema,
    company_monthly_stats_schema,
    personnel_experience_stats_schema
)

app = Flask(__name__)

# Placeholder for BigQuery client initialization.
# In a real application, ensure credentials are configured.
bq_client = bigquery.Client()

@app.route('/')
def home():
    return "Welcome to the BigQuery API!"

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    if not data:
        return jsonify({"message": "Request body must be JSON"}), 400

    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({"message": "Missing username or password"}), 400

    # Mock authentication
    if username == "testuser" and password == "password":
        return jsonify({"message": "Login successful", "user_id": "mock_user_id_123"}), 200
    else:
        return jsonify({"message": "Invalid credentials"}), 401

@app.route('/dashboard_stats', methods=['GET'])
def get_dashboard_stats():
    # Mock data for dashboard statistics
    personnel_count = 1200
    main_contractor_count = 45
    sub_contractor_count = 130
    job_distribution = [
        {'label': 'ME', 'value': 250},  # Mechanical Engineer
        {'label': 'IE', 'value': 200},  # Electrical Engineer
        {'label': 'WT', 'value': 300},  # Welding Technician
        {'label': 'GC', 'value': 150},  # General Construction
        {'label': 'CSA', 'value': 100} # Civil/Structural/Architectural
    ]
    age_distribution = [
        {'range': '20-29', 'count': 150},
        {'range': '30-39', 'count': 300},
        {'range': '40-49', 'count': 250},
        {'range': '50-59', 'count': 180},
        {'range': '60+', 'count': 70}
    ]

    # Mock BigQuery Queries (comments):
    # SELECT COUNT(*) AS personnel_count FROM `your-project.your-dataset.Personnel`;
    # SELECT COUNT(*) AS main_contractor_count FROM `your-project.your-dataset.Companies` WHERE company_type = 'Main Contractor';
    # SELECT COUNT(*) AS sub_contractor_count FROM `your-project.your-dataset.Companies` WHERE company_type = 'Sub Contractor';
    # SELECT job_category, COUNT(*) as count FROM `your-project.your-dataset.Personnel` GROUP BY job_category;
    # SELECT
    #   CASE
    #     WHEN age >= 20 AND age <= 29 THEN '20-29'
    #     WHEN age >= 30 AND age <= 39 THEN '30-39'
    #     WHEN age >= 40 AND age <= 49 THEN '40-49'
    #     WHEN age >= 50 AND age <= 59 THEN '50-59'
    #     WHEN age >= 60 THEN '60+'
    #     ELSE 'Other'
    #   END as age_range,
    #   COUNT(*) as count
    # FROM `your-project.your-dataset.Personnel`
    # GROUP BY age_range;

    return jsonify({
        "personnel_count": personnel_count,
        "main_contractor_count": main_contractor_count,
        "sub_contractor_count": sub_contractor_count,
        "job_distribution": job_distribution,
        "age_distribution": age_distribution
    }), 200

@app.route('/personnel', methods=['GET'])
def get_personnel_list():
    # Mock data based on 2.html
    mock_personnel_data = [
        {
            "name": "王小明", "personnel_id": "P001", "work_id": "W12345", "main_contractor_name": "宏成公司", "sub_contractor_name": "遠東企業",
            "job_category": "ME", "tsmc_experience_years": 5, "professional_licenses": ["高壓電證照"],
            "health_rating": 3, "safety_record": "良好"
        },
        {
            "name": "陳大文", "personnel_id": "P002", "work_id": "W67890", "main_contractor_name": "宏成公司", "sub_contractor_name": "華夏建設",
            "job_category": "IE", "tsmc_experience_years": 3, "professional_licenses": ["室內配線證照"],
            "health_rating": 2, "safety_record": "普通"
        },
        {
            "name": "李美麗", "personnel_id": "P003", "work_id": "W13579", "main_contractor_name": "漢唐集成", "sub_contractor_name": "無",
            "job_category": "WT", "tsmc_experience_years": 8, "professional_licenses": ["焊接證照"],
            "health_rating": 1, "safety_record": "優良"
        },
        {
            "name": "張建國", "personnel_id": "P004", "work_id": "W24680", "main_contractor_name": "漢唐集成", "sub_contractor_name": "利百代工程",
            "job_category": "GC", "tsmc_experience_years": 2, "professional_licenses": [],
            "health_rating": 4, "safety_record": "需注意"
        },
        {
            "name": "林秀英", "personnel_id": "P005", "work_id": "W98765", "main_contractor_name": "互助營造", "sub_contractor_name": "無",
            "job_category": "CSA", "tsmc_experience_years": 10, "professional_licenses": ["工地主任證"],
            "health_rating": 1, "safety_record": "優良"
        }
    ]

    # Filtering logic
    name_filter = request.args.get('name')
    idnum_filter = request.args.get('idnum') # Assuming this maps to personnel_id or work_id
    company_filter = request.args.get('company')
    jobtype_filter = request.args.get('jobtype')

    filtered_personnel = mock_personnel_data

    if name_filter:
        filtered_personnel = [p for p in filtered_personnel if name_filter.lower() in p['name'].lower()]
    if idnum_filter:
        filtered_personnel = [p for p in filtered_personnel if idnum_filter.lower() in p['personnel_id'].lower() or idnum_filter.lower() in p['work_id'].lower()]
    if company_filter:
        filtered_personnel = [p for p in filtered_personnel if company_filter.lower() in p['main_contractor_name'].lower() or company_filter.lower() in p['sub_contractor_name'].lower()]
    if jobtype_filter:
        filtered_personnel = [p for p in filtered_personnel if jobtype_filter.upper() == p['job_category']]


    # Mock BigQuery Query (comment):
    # query = """
    # SELECT *
    # FROM `your-project.your-dataset.Personnel`
    # WHERE
    #   (@name_filter IS NULL OR name LIKE CONCAT('%', @name_filter, '%')) AND
    #   (@idnum_filter IS NULL OR personnel_id LIKE CONCAT('%', @idnum_filter, '%') OR work_id LIKE CONCAT('%', @idnum_filter, '%')) AND
    #   (@company_filter IS NULL OR main_contractor_name LIKE CONCAT('%', @company_filter, '%') OR sub_contractor_name LIKE CONCAT('%', @company_filter, '%')) AND
    #   (@jobtype_filter IS NULL OR job_category = @jobtype_filter)
    # ORDER BY name
    # LIMIT @limit OFFSET @offset;
    # """
    # job_config = bigquery.QueryJobConfig(
    #     query_parameters=[
    #         bigquery.ScalarQueryParameter("name_filter", "STRING", name_filter),
    #         bigquery.ScalarQueryParameter("idnum_filter", "STRING", idnum_filter),
    #         bigquery.ScalarQueryParameter("company_filter", "STRING", company_filter),
    #         bigquery.ScalarQueryParameter("jobtype_filter", "STRING", jobtype_filter),
    #         bigquery.ScalarQueryParameter("limit", "INT64", 10), # Example pagination
    #         bigquery.ScalarQueryParameter("offset", "INT64", 0)
    #     ]
    # )
    # query_job = bq_client.query(query, job_config=job_config)  # Pass job_config
    # results = query_job.result() # Waits for job to complete.
    # personnel_list = [dict(row) for row in results]
    # return jsonify(personnel_list)

    return jsonify(filtered_personnel), 200

# (Re-using the mock_personnel_data from get_personnel_list for simplicity in this mock environment)
mock_personnel_data_for_detail_view = [
    {
        "name": "王小明", "personnel_id": "P001", "work_id": "W12345", "main_contractor_name": "宏成公司", "sub_contractor_name": "遠東企業",
        "job_category": "ME", "tsmc_experience_years": 5, "professional_licenses": ["高壓電證照"],
        "health_rating": 3, "safety_record": "良好", "entry_date": "2023-01-15", "recent_edu_cert_date": "2024-05-20",
        "contact_number": "0912-345-678", "emergency_contact": "王大明 / 0987-654-321"
    },
    {
        "name": "陳大文", "personnel_id": "P002", "work_id": "W67890", "main_contractor_name": "宏成公司", "sub_contractor_name": "華夏建設",
        "job_category": "IE", "tsmc_experience_years": 3, "professional_licenses": ["室內配線證照"],
        "health_rating": 2, "safety_record": "普通", "entry_date": "2022-11-01", "recent_edu_cert_date": "2024-04-10",
        "contact_number": "0922-123-456", "emergency_contact": "陳小美 / 0911-223-344"
    },
    {
        "name": "李美麗", "personnel_id": "P003", "work_id": "W13579", "main_contractor_name": "漢唐集成", "sub_contractor_name": "無",
        "job_category": "WT", "tsmc_experience_years": 8, "professional_licenses": ["焊接證照"],
        "health_rating": 1, "safety_record": "優良", "entry_date": "2020-05-20", "recent_edu_cert_date": "2024-06-01",
        "contact_number": "0933-987-654", "emergency_contact": "李大強 / 0955-876-543"
    },
    {
        "name": "張建國", "personnel_id": "P004", "work_id": "W24680", "main_contractor_name": "漢唐集成", "sub_contract_name": "利百代工程",
        "job_category": "GC", "tsmc_experience_years": 2, "professional_licenses": [],
        "health_rating": 4, "safety_record": "需注意", "entry_date": "2023-08-01", "recent_edu_cert_date": "2024-03-15",
        "contact_number": "0944-111-222", "emergency_contact": "張太太 / 0966-222-333"
    },
    {
        "name": "林秀英", "personnel_id": "P005", "work_id": "W98765", "main_contractor_name": "互助營造", "sub_contractor_name": "無",
        "job_category": "CSA", "tsmc_experience_years": 10, "professional_licenses": ["工地主任證"],
        "health_rating": 1, "safety_record": "優良", "entry_date": "2018-03-10", "recent_edu_cert_date": "2024-05-01",
        "contact_number": "0955-777-888", "emergency_contact": "林先生 / 0977-888-999"
    }
]

@app.route('/personnel/<string:personnel_id>', methods=['GET'])
def get_personnel_detail(personnel_id):
    # Mock BigQuery Query (comment):
    # query = "SELECT * FROM `your-project.your-dataset.Personnel` WHERE personnel_id = @personnel_id;"
    # job_config = bigquery.QueryJobConfig(
    #     query_parameters=[
    #         bigquery.ScalarQueryParameter("personnel_id", "STRING", personnel_id),
    #     ]
    # )
    # query_job = bq_client.query(query, job_config=job_config)
    # results = list(query_job.result())
    # if results:
    #    return jsonify(dict(results[0]))
    # else:
    #    return jsonify({"message": "Personnel not found"}), 404

    for person in mock_personnel_data_for_detail_view: # Using the more detailed mock data
        if person['personnel_id'] == personnel_id:
            return jsonify(person)
    return jsonify({"message": "Personnel not found"}), 404

@app.route('/companies', methods=['GET'])
def get_companies_list():
    # Mock BigQuery Query (comment):
    # SELECT company_id, name, type, parent_company_id FROM `your-project.your-dataset.Companies` ORDER BY name;

    mock_companies_data = [
        {"company_id": "C001", "name": "宏成公司", "type": "Main Contractor"},
        {"company_id": "C002", "name": "建億公司", "type": "Main Contractor"},
        {"company_id": "C003", "name": "盛達建設", "type": "Main Contractor"},
        {"company_id": "C004", "name": "漢唐集成", "type": "Main Contractor"},
        {"company_id": "C005", "name": "互助營造", "type": "Main Contractor"},
        {"company_id": "S001", "name": "遠東企業", "type": "Sub Contractor", "parent_company_id": "C001"},
        {"company_id": "S002", "name": "大同企業", "type": "Sub Contractor", "parent_company_id": "C002"},
        {"company_id": "S003", "name": "華夏建設", "type": "Sub Contractor", "parent_company_id": "C001"},
        {"company_id": "S004", "name": "利百代工程", "type": "Sub Contractor", "parent_company_id": "C004"},
        {"company_id": "S005", "name": "新進工程", "type": "Sub Contractor", "parent_company_id": "C003"},
        {"company_id": "S006", "name": "世紀鋼", "type": "Sub Contractor", "parent_company_id": "C005"},
    ]
    return jsonify(mock_companies_data), 200

# This list is used by get_company_stats for company name lookup.
# It's a simplified version of mock_companies_data from get_companies_list.
mock_companies_data_for_stats_lookup = [
    {"company_id": "C001", "name": "宏成公司", "type": "Main Contractor"},
    {"company_id": "C002", "name": "建億公司", "type": "Main Contractor"},
    {"company_id": "C003", "name": "盛達建設", "type": "Main Contractor"},
    {"company_id": "C004", "name": "漢唐集成", "type": "Main Contractor"},
    {"company_id": "C005", "name": "互助營造", "type": "Main Contractor"},
    {"company_id": "S001", "name": "遠東企業", "type": "Sub Contractor", "parent_company_id": "C001"},
    {"company_id": "S002", "name": "大同企業", "type": "Sub Contractor", "parent_company_id": "C002"},
    {"company_id": "S003", "name": "華夏建設", "type": "Sub Contractor", "parent_company_id": "C001"},
    {"company_id": "S004", "name": "利百代工程", "type": "Sub Contractor", "parent_company_id": "C004"},
    {"company_id": "S005", "name": "新進工程", "type": "Sub Contractor", "parent_company_id": "C003"},
    {"company_id": "S006", "name": "世紀鋼", "type": "Sub Contractor", "parent_company_id": "C005"},
]

@app.route('/companies/<string:company_id>/stats', methods=['GET'])
def get_company_stats(company_id):
    company = next((c for c in mock_companies_data_for_stats_lookup if c['company_id'] == company_id), None)
    if not company:
        return jsonify({"message": "Company not found"}), 404

    # Mock BigQuery Queries (comments):
    # Basic Company Stats: SELECT name, current_manpower, peak_manpower, avg_manpower_last_month, overall_evaluation_score, high_risk_personnel_ratio, violation_ratio FROM `your-project.your-dataset.Companies` WHERE company_id = @company_id;
    # Experience Distribution:
    #   SELECT
    #     CASE
    #       WHEN SUM(CASE WHEN pes.experience_years < 1 THEN 1 ELSE 0 END) > 0 THEN '<1年'
    #       WHEN SUM(CASE WHEN pes.experience_years >= 1 AND pes.experience_years < 3 THEN 1 ELSE 0 END) > 0 THEN '1-3年'
    #       WHEN SUM(CASE WHEN pes.experience_years >= 3 AND pes.experience_years < 5 THEN 1 ELSE 0 END) > 0 THEN '3-5年'
    #       WHEN SUM(CASE WHEN pes.experience_years >= 5 AND pes.experience_years < 10 THEN 1 ELSE 0 END) > 0 THEN '5-10年'
    #       ELSE '>10年'
    #     END as experience_range, COUNT(p.personnel_id) as count
    #   FROM `your-project.your-dataset.Personnel` p
    #   LEFT JOIN `your-project.your-dataset.PersonnelExperienceStats` pes ON p.personnel_id = pes.personnel_id  // Assuming TSMC experience is primary
    #   WHERE p.company_id = @company_id // Assuming Personnel table has a direct company_id for their current primary assignment
    #   GROUP BY experience_range;
    # Monthly Trends (Violations, Job Types, Health Risks):
    #   SELECT month, total_violations, me_personnel_count, gc_personnel_count,csa_personnel_count, wt_personnel_count, ie_personnel_count, other_personnel_count, health_risk_level1_count, health_risk_level2_count, health_risk_level3_count, health_risk_level4_count, health_risk_level5_count
    #   FROM `your-project.your-dataset.CompanyMonthlyStats`
    #   WHERE company_id = @company_id
    #   ORDER BY month DESC LIMIT 12;

    # Default values, can be overridden for specific companies like C001
    manpower = 100
    max_manpower = 75
    avg_manpower = 65
    evaluation_score = 88
    high_risk_health_personnel_percentage = 15.0
    violation_personnel_percentage = 10.0
    current_violations_june = 3 # Default for June 2024

    job_dist_june_default = {"me": 20, "gc": 20, "csa": 15, "wt": 10, "ie": 10, "other": 25} # sum 100
    health_dist_june_default = {"level1": 5, "level2": 10, "level3": 25, "level4": 35, "level5": 25} # sum 100

    if company_id == "C001": # 宏成公司 specific values from 4.html
        manpower = 120
        max_manpower = 85
        avg_manpower = 70
        evaluation_score = 92
        high_risk_health_personnel_percentage = 12.0
        violation_personnel_percentage = 8.0
        current_violations_june = 5
        job_dist_june_default = {"me": 20, "gc": 25, "csa": 15, "wt": 10, "ie": 10, "other": 40} # sum 120
        health_dist_june_default = {"level1": 5, "level2": 10, "level3": 30, "level4": 40, "level5": 35} # sum 120


    mock_company_stats = {
        "company_id": company_id,
        "name": company['name'],
        "manpower": manpower,
        "max_manpower": max_manpower,
        "avg_manpower": avg_manpower,
        "evaluation_score": evaluation_score,
        "high_risk_health_personnel_percentage": high_risk_health_personnel_percentage,
        "violation_personnel_percentage": violation_personnel_percentage,
        "experience_distribution": [
            {"label": "<1年", "count": round(manpower * 0.12)},
            {"label": "1-3年", "count": round(manpower * 0.28)},
            {"label": "3-5年", "count": round(manpower * 0.35)},
            {"label": "5-10年", "count": round(manpower * 0.18)},
            {"label": ">10年", "count": manpower - round(manpower*0.12) - round(manpower*0.28) - round(manpower*0.35) - round(manpower*0.18)} # Ensure sum matches manpower
        ],
        "violation_trend": [ # Last 12 months
            {"month": "2023/07", "count": 2 if company_id != "C001" else 3}, {"month": "2023/08", "count": 3 if company_id != "C001" else 4},
            {"month": "2023/09", "count": 1 if company_id != "C001" else 2}, {"month": "2023/10", "count": 4 if company_id != "C001" else 5},
            {"month": "2023/11", "count": 2 if company_id != "C001" else 3}, {"month": "2023/12", "count": 4 if company_id != "C001" else 6}, # C001 had 5 in one example, 6 here for variation
            {"month": "2024/01", "count": 3 if company_id != "C001" else 4}, {"month": "2024/02", "count": 2 if company_id != "C001" else 3},
            {"month": "2024/03", "count": 4 if company_id != "C001" else 5}, {"month": "2024/04", "count": 3 if company_id != "C001" else 4},
            {"month": "2024/05", "count": 3 if company_id != "C001" else 4}, {"month": "2024/06", "count": current_violations_june}
        ],
        "job_type_distribution_trend": [ # Last 3 months example
            {"month": "2024/04", "me": round(avg_manpower*0.24), "gc": round(avg_manpower*0.20), "csa": round(avg_manpower*0.16), "wt": round(avg_manpower*0.10), "ie": round(avg_manpower*0.14), "other": avg_manpower - round(avg_manpower*0.24)-round(avg_manpower*0.20)-round(avg_manpower*0.16)-round(avg_manpower*0.10)-round(avg_manpower*0.14)},
            {"month": "2024/05", "me": round(avg_manpower*0.25), "gc": round(avg_manpower*0.21), "csa": round(avg_manpower*0.15), "wt": round(avg_manpower*0.11), "ie": round(avg_manpower*0.13), "other": avg_manpower - round(avg_manpower*0.25)-round(avg_manpower*0.21)-round(avg_manpower*0.15)-round(avg_manpower*0.11)-round(avg_manpower*0.13)},
            {"month": "2024/06", **job_dist_june_default}
        ],
        "health_risk_distribution_trend": [ # Last 3 months example
            {"month": "2024/04", "level1": round(avg_manpower*0.04), "level2": round(avg_manpower*0.08), "level3": round(avg_manpower*0.30), "level4": round(avg_manpower*0.40), "level5": avg_manpower - round(avg_manpower*0.04)-round(avg_manpower*0.08)-round(avg_manpower*0.30)-round(avg_manpower*0.40)},
            {"month": "2024/05", "level1": round(avg_manpower*0.05), "level2": round(avg_manpower*0.09), "level3": round(avg_manpower*0.28), "level4": round(avg_manpower*0.38), "level5": avg_manpower - round(avg_manpower*0.05)-round(avg_manpower*0.09)-round(avg_manpower*0.28)-round(avg_manpower*0.38)},
            {"month": "2024/06", **health_dist_june_default}
        ]
    }
    return jsonify(mock_company_stats)

# Using mock_companies_data_for_stats_lookup for company existence check
# Mock project data, assuming some association with company_id
mock_project_history_data = [
    {"project_id": "PRJ001", "project_name": "南科晶圓擴建", "start_date": "2022/03", "end_date": "2022/12", "participant_count": 80, "company_id_associated": "C001", "status": "Completed"},
    {"project_id": "PRJ002", "project_name": "竹科新廠建置", "start_date": "2023/01", "end_date": "2023/11", "participant_count": 95, "company_id_associated": "C001", "status": "Completed"},
    {"project_id": "PRJ003", "project_name": "中科擴廠工程", "start_date": "2024/02", "end_date": "2024/10", "participant_count": 110, "company_id_associated": "C001", "status": "In Progress"},
    {"project_id": "PRJ004", "project_name": "高雄新廠A", "start_date": "2023/05", "end_date": "2023/12", "participant_count": 50, "company_id_associated": "C002", "status": "Completed"},
    {"project_id": "PRJ005", "project_name": "南科F18廠維護", "start_date": "2024/01", "end_date": "Ongoing", "participant_count": 40, "company_id_associated": "C002", "status": "In Progress"},
    {"project_id": "PRJ006", "project_name": "竹科P8廠升級", "start_date": "2023/08", "end_date": "2024/03", "participant_count": 70, "company_id_associated": "C003", "status": "Completed"},
    {"project_id": "PRJ007", "project_name": "中科AP5建廠", "start_date": "2022/10", "end_date": "Ongoing", "participant_count": 120, "company_id_associated": "C004", "status": "In Progress"},
    {"project_id": "PRJ008", "project_name": "路竹新廠初期", "start_date": "2024/05", "end_date": "Ongoing", "participant_count": 60, "company_id_associated": "C001", "status": "Planning"},
    {"project_id": "PRJ009", "project_name": "SubProject Alpha for C001", "start_date": "2023/06", "end_date": "2023/09", "participant_count": 25, "company_id_associated": "S001", "status": "Completed"}, # S001 is sub of C001
]

@app.route('/companies/<string:company_id>/projects', methods=['GET'])
def get_company_projects(company_id):
    # Find Company (to ensure company_id is valid)
    company_exists = next((c for c in mock_companies_data_for_stats_lookup if c['company_id'] == company_id), None)
    if not company_exists:
        return jsonify({"message": "Company not found"}), 404

    # Mock BigQuery Query (comment):
    # SELECT p.project_id, p.name as project_name, cp.start_date, cp.end_date, cp.current_personnel_count as participant_count, p.status
    # FROM `your-project.your-dataset.Projects` p
    # JOIN `your-project.your-dataset.CompanyProjects` cp ON p.project_id = cp.project_id
    # WHERE cp.company_id = @company_id
    # ORDER BY cp.start_date DESC;
    # Note: If subcontractors' projects are also to be listed under main, the query would be more complex,
    # potentially joining Companies table to find subs of the main_company_id or handling it in application logic.

    # Filter projects for the given company_id.
    # This basic mock version only shows projects directly associated with the company_id.
    # For main contractors, one might also want to show projects of their subcontractors.
    filtered_projects = [
        proj for proj in mock_project_history_data if proj['company_id_associated'] == company_id
    ]

    # If the company is a Main Contractor, also include projects from its direct subcontractors.
    if company_exists['type'] == 'Main Contractor':
        sub_contractor_ids = [
            sub['company_id'] for sub in mock_companies_data_for_stats_lookup
            if sub.get('parent_company_id') == company_id and sub['type'] == 'Sub Contractor'
        ]
        for sub_id in sub_contractor_ids:
            sub_projects = [
                proj for proj in mock_project_history_data if proj['company_id_associated'] == sub_id
            ]
            # Add a marker or modify project name to indicate it's a sub-contractor's project if needed
            for sp in sub_projects:
                if sp not in filtered_projects: # Avoid duplicates if a project somehow got associated with both
                    # Optionally, mark that this project belongs to a sub-contractor
                    # sp['original_company_name'] = next((s['name'] for s in mock_companies_data_for_stats_lookup if s['company_id'] == sub_id), '')
                    filtered_projects.append(sp)


    return jsonify(filtered_projects)

# Using mock_companies_data_for_stats_lookup (which is a copy of mock_companies_data) for this
@app.route('/subcontractors_for_main/<string:main_contractor_id>', methods=['GET'])
def get_subcontractors_for_main(main_contractor_id):
    # Validate Main Contractor ID
    main_contractor = next((c for c in mock_companies_data_for_stats_lookup if c['company_id'] == main_contractor_id and c['type'] == 'Main Contractor'), None)
    if not main_contractor:
        return jsonify({"message": "Main contractor not found"}), 404

    # Mock BigQuery Query (comment):
    # SELECT company_id, name FROM `your-project.your-dataset.Companies`
    # WHERE type = 'Sub Contractor' AND parent_company_id = @main_contractor_id
    # ORDER BY name;

    # Filter mock_companies_data to find subcontractors
    subcontractors = [
        company for company in mock_companies_data_for_stats_lookup
        if company['type'] == 'Sub Contractor' and company.get('parent_company_id') == main_contractor_id
    ]

    return jsonify(subcontractors), 200

if __name__ == '__main__':
    # Note: This is for development only.
    # Note: This is for development only.
    # Use a production WSGI server like Gunicorn in production.
    app.run(debug=True, host='0.0.0.0', port=8080)
