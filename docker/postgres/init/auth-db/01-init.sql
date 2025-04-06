-- Schema initialization for auth database
CREATE SCHEMA IF NOT EXISTS auth;

-- Users table to store authentication information
CREATE TABLE IF NOT EXISTS auth.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing OAuth2 client details
CREATE TABLE IF NOT EXISTS auth.oauth2_clients (
    id SERIAL PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL UNIQUE,
    client_secret VARCHAR(255) NOT NULL,
    authorized_grant_types VARCHAR(255) NOT NULL,
    scope VARCHAR(255) NOT NULL,
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing refresh tokens
CREATE TABLE IF NOT EXISTS auth.refresh_tokens (
    token_id VARCHAR(255) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES auth.users(id),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_user_email ON auth.users(email);
CREATE INDEX IF NOT EXISTS idx_user_username ON auth.users(username);