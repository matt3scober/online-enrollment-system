-- Schema initialization for auth database
CREATE SCHEMA IF NOT EXISTS auth;

-- Roles table
CREATE TABLE IF NOT EXISTS auth.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Users table to store authentication information
CREATE TABLE IF NOT EXISTS auth.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Junction table for users and roles (many-to-many)
CREATE TABLE IF NOT EXISTS auth.user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth.users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES auth.roles(id) ON DELETE CASCADE
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

-- Create roles if they don't exist
INSERT INTO auth.roles (name) VALUES ('USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO auth.roles (name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO auth.roles (name) VALUES ('MODERATOR') ON CONFLICT (name) DO NOTHING;