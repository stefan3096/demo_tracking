# demo_tracking
this demo for system Rest API for tracking sistem

url kafka ui for manage message
http://13.53.92.120:8080/

1. you need mvn clean install after pull
2. make sure you connection
3. follow installation of kafka, postgre, and jenkins also in you repository


for testing endpoint succes handling self get data from kafka
use post Data
curl --location 'http://13.48.138.238:8087/test/transaction' \
--header 'Content-Type: application/json' \
--data-raw '{
"location": "Jakarta Selatan",
"senderName": "Andrika",
"senderEmail": "Andrika@gmail.com",
"nik": 317203223009960004,
"receiverName": "Siti",
"branchCode": "BR001",
"cityCode": "CGK",
"senderAddress": "Jl. Sudirman No. 123, Jakarta",
"workerId": "JKT-STH-L1",
"receiverAddress": "Jl. Thamrin No. 456, Jakarta",
"phoneNumber": "081234567890",
"message": "Package for urgent delivery",
"description": "Electronics item - handle with care",
"statusCode": "PICKED_UP",
"weight":2.3
}'


for get Data By Customer Id
curl --location 'http://13.48.138.238:8087/test/customer/fb3e39c6-6c38-4ebc-9e4c-ad15b2a159ab'