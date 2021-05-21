import http.client
import json
import ssl
import os
import base64

from cryptography.hazmat.primitives import serialization as crypto_serialization
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.backends import default_backend as crypto_default_backend
import cryptography.x509 as crypto_cert

# Private and public key file names
private_key_path = 'key.priv'
public_key_path = 'key.pub'

# Certificate file name
certificate_file_path = 'pressure_device_sert.crt'
certificate_secret = 'sadpotato'

# Host of endpoint
api_hospital_root = 'localhost:9002'

# Endpoints
endpoint_request_cert = '/api/devices/request-cert'
endpoint_send_data = '/api/devices/data'


def generate_keys():
    key = rsa.generate_private_key(
        backend=crypto_default_backend(),
        public_exponent=65537,
        key_size=2048
    )

    private_key = key.private_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PrivateFormat.PKCS8,
        crypto_serialization.NoEncryption())

    public_key = key.public_key().public_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PublicFormat.SubjectPublicKeyInfo
    )

    print(private_key)
    print(public_key)

    file_private = open(private_key_path, 'w')
    file_private.write(bytes.decode(private_key, 'utf-8'))
    file_private.close()

    file_public = open(public_key_path, 'w')
    file_public.write(bytes.decode(public_key, 'utf-8'))
    file_public.close()

    return private_key, public_key


def read_keys():
    file_private = open(private_key_path, 'rb')
    private_key = crypto_serialization.load_pem_private_key(file_private.read(), password=None)

    file_private.close()

    file_public = open(public_key_path, 'rb')
    public_key = crypto_serialization.load_pem_public_key(file_public.read())
    file_public.close()

    private_key = private_key.private_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PrivateFormat.PKCS8,
        crypto_serialization.NoEncryption())

    public_key = public_key.public_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PublicFormat.SubjectPublicKeyInfo
    )

    print(private_key)
    print(public_key)

    return private_key, public_key


def request_cert(private_key, public_key):
    # Define the client certificate settings for https connection

    # Create a connection to submit HTTP requests
    connection = http.client.HTTPConnection(api_hospital_root)

    # Defining parts of the HTTP request
    request_headers = {
        'Content-Type': 'application/json'
    }
    common_name = 'TestDevice'
    organization = 'Hospital1'
    organization_unit = 'Hospital1'
    locality = 'Novi Sad'
    state = 'Vojvodina'
    country = 'RS'
    email = 'admin1@gmail.com'
    public_key = bytes.decode(public_key, 'utf-8')

    request_body_dict = {
        'commonName': common_name,
        'organization': organization,
        'organizationUnit': organization_unit,
        'locality': locality,
        'state': state,
        'country': country,
        'email': email,
        'publicKey': public_key
    }
    private_key = crypto_serialization.load_pem_private_key(private_key, password=None)
    request_body_dict['signature'] = private_key.sign(
        data=bytes(common_name + organization + organization_unit + locality + state + country + email, 'utf-8'),
        padding=padding.PKCS1v15(),
        algorithm =hashes.SHA1()
    )
    request_body_dict['signature'] = base64.b64encode(request_body_dict['signature']).decode('utf-8')

    # Use connection to submit a HTTP POST request
    connection.request(method="POST", url=endpoint_request_cert, headers=request_headers, body=json.dumps(request_body_dict))

    # Print the HTTP response from the IOT service endpoint
    response = connection.getresponse()
    print(response.status, response.reason)
    data = response.read()
    print(data)


def establish_connection():
    file_cert = open(certificate_file_path, 'rb')
    cert = crypto_cert.load_pem_x509_certificate(file_cert.read())

    # Define the client certificate settings for https connection
    context = ssl.create_default_context()
    # context.load_cert_chain()
    context.load_cert_chain(certfile=certificate_file_path, password=certificate_secret, keyfile=private_key_path)

    # Create a connection to submit HTTP requests
    connection = http.client.HTTPSConnection(api_hospital_root, context=context)
    return connection


def send_data(connection, private_key):
    # neki deo sa kriptovanjem sa private key?

    # Defining parts of the HTTP request
    request_headers = {
        'Content-Type': 'application/json'
    }

    request_body_dict = {
        'message': 'Hi I am a device and this message is securely transmitted!'
    }

    # Use connection to submit a HTTP POST request
    connection.request(method="POST", url=endpoint_send_data, headers=request_headers, body=json.dumps(request_body_dict))

    # Print the HTTP response from the IOT service endpoint
    response = connection.getresponse()
    print(response.status, response.reason)
    data = response.read()
    print(data)


if __name__ == '__main__':
    if (not os.path.isfile(private_key_path)) or (not os.path.isfile(public_key_path)):
        private_key, public_key = generate_keys()
    else:
        private_key, public_key = read_keys()
    if not os.path.isfile(certificate_file_path):
        request_cert(private_key, public_key)
    else:
        connection = establish_connection()
        send_data(connection, private_key)
