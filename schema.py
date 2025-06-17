from google.cloud import bigquery
from google.cloud.bigquery import SchemaField

users_schema = [
    SchemaField('user_id', 'STRING', mode='REQUIRED'),
    SchemaField('user_name', 'STRING', mode='REQUIRED'),
    SchemaField('user_email', 'STRING', mode='REQUIRED'),
    SchemaField('user_type', 'STRING'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

personnel_schema = [
    SchemaField('personnel_id', 'STRING', mode='REQUIRED'),
    SchemaField('user_id', 'STRING', mode='REQUIRED'),
    SchemaField('first_name', 'STRING', mode='REQUIRED'),
    SchemaField('last_name', 'STRING', mode='REQUIRED'),
    SchemaField('email', 'STRING', mode='REQUIRED'),
    SchemaField('phone_number', 'STRING'),
    SchemaField('address', 'STRING'),
    SchemaField('city', 'STRING'),
    SchemaField('state', 'STRING'),
    SchemaField('zip_code', 'STRING'),
    SchemaField('country', 'STRING'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

companies_schema = [
    SchemaField('company_id', 'STRING', mode='REQUIRED'),
    SchemaField('company_name', 'STRING', mode='REQUIRED'),
    SchemaField('company_domain', 'STRING'),
    SchemaField('company_industry', 'STRING'),
    SchemaField('company_size', 'STRING'),
    SchemaField('company_location', 'STRING'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

projects_schema = [
    SchemaField('project_id', 'STRING', mode='REQUIRED'),
    SchemaField('project_name', 'STRING', mode='REQUIRED'),
    SchemaField('project_description', 'STRING'),
    SchemaField('project_status', 'STRING'),
    SchemaField('start_date', 'DATE'),
    SchemaField('end_date', 'DATE'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

company_projects_schema = [
    SchemaField('company_project_id', 'STRING', mode='REQUIRED'),
    SchemaField('company_id', 'STRING', mode='REQUIRED'),
    SchemaField('project_id', 'STRING', mode='REQUIRED'),
    SchemaField('start_date', 'DATE'),
    SchemaField('end_date', 'DATE'),
    SchemaField('status', 'STRING'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

company_monthly_stats_schema = [
    SchemaField('company_id', 'STRING', mode='REQUIRED'),
    SchemaField('month', 'DATE', mode='REQUIRED'),
    SchemaField('active_users', 'INTEGER'),
    SchemaField('projects_created', 'INTEGER'),
    SchemaField('revenue', 'FLOAT'),
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]

personnel_experience_stats_schema = [
    SchemaField('personnel_id', 'STRING', mode='REQUIRED'),
    SchemaField('skill_id', 'STRING', mode='REQUIRED'), # Assuming a separate skills table
    SchemaField('experience_years', 'INTEGER'),
    SchemaField('skill_level', 'STRING'), # e.g., Beginner, Intermediate, Advanced
    SchemaField('created_at', 'TIMESTAMP'),
    SchemaField('updated_at', 'TIMESTAMP'),
]
