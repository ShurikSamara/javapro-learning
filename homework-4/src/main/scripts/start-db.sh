#!/bin/bash
echo "Запуск PostgreSQL в Docker..."
docker-compose up -d postgres
echo "Ожидание готовности базы данных..."
sleep 10
echo "База данных готова!"