#!/usr/bin/env bash
set -euo pipefail

: "${DB_HOST:=localhost}"
: "${DB_PORT:=5432}"
: "${DB_NAME:=order_inventory_db}"
: "${DB_USERNAME:=postgres}"

pg_dump -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USERNAME" "$DB_NAME"

