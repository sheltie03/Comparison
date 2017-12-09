# generate server.xml with the following command:
#    openssl req -new -x509 -keyout server.pem -out server.pem -days 365 -nodes
# run as follows:
#    python ssl_server.py
# then in your browser, visit:
#    https://localhost:4443

import BaseHTTPServer, SimpleHTTPServer
import ssl

httpd = BaseHTTPServer.HTTPServer(('localhost', 4443), SimpleHTTPServer.SimpleHTTPRequestHandler)

### python 2.6
# httpd.socket = ssl.wrap_socket (httpd.socket, certfile='./server.pem', server_side=True, ssl_version=ssl.PROTOCOL_SSLv2)

# httpd.socket = ssl.wrap_socket (httpd.socket, certfile='./server.pem', server_side=True, ssl_version=ssl.PROTOCOL_SSLv3)

### python 2.7
# httpd.socket = ssl.wrap_socket (httpd.socket, certfile='./server.pem', server_side=True, ssl_version=ssl.PROTOCOL_TLSv1)

httpd.socket = ssl.wrap_socket (httpd.socket, certfile='./server.pem', server_side=True, ssl_version=ssl.PROTOCOL_TLSv1_1)

# httpd.socket = ssl.wrap_socket (httpd.socket, certfile='./server.pem', server_side=True, ssl_version=ssl.PROTOCOL_TLSv1_2)

# httpd.handle_request()
httpd.serve_forever()
