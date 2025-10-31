-- Crear keyspace si no existe
CREATE KEYSPACE IF NOT EXISTS billetera WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

-- Usar el keyspace
USE billetera;

-- Crear tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  dni TEXT PRIMARY KEY,
  nombre TEXT,
  saldo DECIMAL
);

-- Crear tabla movimientos
CREATE TABLE IF NOT EXISTS movimientos (
  id UUID PRIMARY KEY,
  dni_origen TEXT,
  dni_destino TEXT,
  monto DECIMAL,
  mensaje TEXT,
  codigo_transferencia TEXT,
  fecha_hora TIMESTAMP
);
