# SSL

## Create Self-Signed Certificate

This is how the self-signed certificate was created for this project.

The CN is `demo.local`.

```
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 3650 -nodes -subj "/C=US/ST=NY/L=New York/CN=demo.local"
```