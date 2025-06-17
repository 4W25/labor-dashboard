# Use an official Python runtime as a parent image
FROM python:3.9-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
ENV PORT 8080

# Set the working directory in the container
WORKDIR /app

# Install system dependencies if any (for now, none are specified)
# RUN apt-get update && apt-get install -y ...

# Copy the requirements file into the container at /app
COPY requirements.txt .

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code into the container at /app
COPY main.py .
COPY schema.py .

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
# The default host 0.0.0.0 makes the app accessible from outside the container
# The number of workers can be adjusted based on expected load
CMD exec gunicorn --bind :$PORT --workers 1 --threads 8 --timeout 0 main:app
