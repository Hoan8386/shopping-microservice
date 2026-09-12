# Du lieu mau cho Product Service

File nay chua san URL va JSON body de copy vao Postman.

## 1. Cau hinh

- Product service: `http://localhost:9002`
- Header cho request co body: `Content-Type: application/json`

Tat ca ID deu do server tu sinh. Sau moi request tao, copy gia tri `id` trong response vao bien tuong ung:

```json
{
  "id": "id-duoc-server-sinh"
}
```

Bien su dung trong cac request ben duoi: `categoryId`, `sizeSId`, `sizeMId`, `productId`, `productDetailId`.

## 2. Tao category

**POST** `http://localhost:9002/api/v1/category`

```json
{
  "name": "Ao nam",
  "description": "Danh muc ao thoi trang nam",
  "status": true
}
```

**POST** `http://localhost:9002/api/v1/category`

```json
{
  "name": "Quan nam",
  "description": "Danh muc quan thoi trang nam",
  "status": true
}
```

## 3. Tao size

**POST** `http://localhost:9002/api/v1/size`

```json
{
  "name": "S",
  "description": "Size nho",
  "status": true
}
```

**POST** `http://localhost:9002/api/v1/size`

```json
{
  "name": "M",
  "description": "Size vua",
  "status": true
}
```

## 4. Tao product

Thay `categoryId` bang ID category nhan duoc o buoc 2.

**POST** `http://localhost:9002/api/v1/products`

```json
{
  "name": "Ao thun cotton basic",
  "description": "Ao thun cotton 100%, phom regular, phu hop mac hang ngay",
  "price": 199000,
  "quantity": 100,
  "idCategory": "categoryId",
  "imageUrl": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab"
}
```

**POST** `http://localhost:9002/api/v1/products`

```json
{
  "name": "Quan jeans slim fit",
  "description": "Quan jeans xanh dang slim fit",
  "price": 499000,
  "quantity": 50,
  "idCategory": "categoryId",
  "imageUrl": "https://images.unsplash.com/photo-1542272604-787c3835535d"
}
```

## 5. Tao product detail theo size

Thay `productId` bang ID product va `sizeSId` bang ID size S.

**POST** `http://localhost:9002/api/v1/product-details`

```json
{
  "productId": "productId",
  "sizeId": "sizeSId",
  "quantity": 40,
  "price": 199000,
  "status": true
}
```

Thay `sizeSId` bang `sizeMId` de tao variant size M:

```json
{
  "productId": "productId",
  "sizeId": "sizeMId",
  "quantity": 60,
  "price": 199000,
  "status": true
}
```

## 6. Xem du lieu

```text
GET http://localhost:9002/api/v1/category
GET http://localhost:9002/api/v1/size
GET http://localhost:9002/api/v1/products
GET http://localhost:9002/api/v1/product-details
GET http://localhost:9002/api/v1/products/filter?name=ao&category=&minPrice=100000&maxPrice=300000
```

Xem chi tiet theo ID:

```text
GET http://localhost:9002/api/v1/category/{categoryId}
GET http://localhost:9002/api/v1/size/{sizeId}
GET http://localhost:9002/api/v1/products/{productId}
GET http://localhost:9002/api/v1/product-details/{productDetailId}
```

## 7. Cap nhat du lieu

### Category

**POST** `http://localhost:9002/api/v1/category/{categoryId}`

```json
{
  "name": "Ao nam cao cap",
  "description": "Danh muc ao nam da cap nhat",
  "status": true
}
```

### Size

**PUT** `http://localhost:9002/api/v1/size/{sizeId}`

```json
{
  "name": "M",
  "description": "Size vua - da cap nhat",
  "status": true
}
```

### Product

**PUT** `http://localhost:9002/api/v1/products/{productId}`

```json
{
  "name": "Ao thun cotton basic - updated",
  "description": "Ao thun cotton da cap nhat",
  "price": 219000,
  "quantity": 80,
  "idCategory": "categoryId",
  "imageUrl": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab"
}
```

### Product detail

**PUT** `http://localhost:9002/api/v1/product-details/{productDetailId}`

```json
{
  "productId": "productId",
  "sizeId": "sizeSId",
  "quantity": 35,
  "price": 219000,
  "status": true
}
```

## 8. Xoa du lieu

Nen xoa product detail truoc, sau do xoa product/category/size neu khong con du lieu phu thuoc.

```text
DELETE http://localhost:9002/api/v1/product-details/{productDetailId}
DELETE http://localhost:9002/api/v1/products/{productId}
DELETE http://localhost:9002/api/v1/category/{categoryId}
DELETE http://localhost:9002/api/v1/size/{sizeId}
```
