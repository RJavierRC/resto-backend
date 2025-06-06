# Resto – Backend

## Puesta en marcha con Docker
[backend_integration_guide_restaurant.pdf](https://github.com/user-attachments/files/20575293/backend_integration_guide_restaurant.pdf)

## Diagrama de base de datos
![image](https://github.com/user-attachments/assets/4cd2cfb9-657d-4ae9-8eb2-fca5483c2d53)

## Notas:
## Base de Datos : Modelo y Referencia SQL

### 🖇️ Relaciones clave
| Relación | Descripción |
|----------|-------------|
| **table_resto (1) → orders (n)** | Una mesa puede tener muchas órdenes a lo largo del día, pero **solo una** con `status = BUSY` a la vez (regla aplicada en el código). |
| **orders (1) → order_item (n)** | Cada orden posee muchas líneas (productos × cantidad). |
| **order_item → product** | Guarda `price_snapshot` para conservar el precio aunque el producto cambie con el tiempo. |

---

### 📜 Script de referencia (PostgreSQL)

> *Las tablas se crean automáticamente con Spring Data JPA (`ddl-auto=update`),  
>  pero el script es útil para inspección o migraciones.*

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. user
CREATE TABLE "user" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  username TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL CHECK (role IN ('ADMIN','WAITER'))
);

-- 2. table_resto
CREATE TABLE table_resto (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  number INT UNIQUE NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('FREE','BUSY','CLOSED'))
);

-- 3. product
CREATE TABLE product (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT NOT NULL,
  category TEXT NOT NULL CHECK (category IN ('FOOD','DRINK','DESSERT')),
  price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 4. orders
CREATE TABLE orders (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  table_id UUID NOT NULL REFERENCES table_resto(id),
  opened_at TIMESTAMP NOT NULL DEFAULT NOW(),
  closed_at TIMESTAMP,
  tip NUMERIC(10,2) NOT NULL DEFAULT 0,
  payment_type TEXT CHECK (payment_type IN ('CASH','CARD'))
);

-- 5. order_item
CREATE TABLE order_item (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  product_id UUID NOT NULL REFERENCES product(id),
  qty INT NOT NULL CHECK (qty > 0),
  price_snapshot NUMERIC(10,2) NOT NULL
);


## Lo que tienes que hacer nomas en teoria es:
```bash
git clone https://github.com/TUUSUARIO/resto-backend.git
cd resto-backend
docker compose up -d --build

#notas:
# API → http://localhost:8080
# Swagger → http://localhost:8080/swagger-ui.html


Admin default: admin / admin123
Mesero demo: juan / waiter1



