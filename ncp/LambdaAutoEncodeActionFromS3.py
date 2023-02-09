import hashlib
import hmac
import base64
import requests
import time
import json


def make_signature(url, timestamp, access_key, secret_key):
    timestamp = int(time.time() * 1000)
    timestamp = str(timestamp)

    secret_key = bytes(secret_key, "UTF-8")

    method = "PUT"

    message = method + " " + url + "\n" + timestamp + "\n" + access_key
    message = bytes(message, "UTF-8")
    sign_key = base64.b64encode(
        hmac.new(secret_key, message, digestmod=hashlib.sha256).digest()
    )
    print(sign_key)
    return sign_key


def make_header(timestamp, access_key, sign_key):
    headers = {
        "Content-Type": "application/json; charset=utf-8",
        "x-ncp-apigw-timestamp": timestamp,
        "x-ncp-iam-access-key": access_key,
        "x-ncp-apigw-signature-v2": sign_key,
    }
    return headers


def main(args):
    object_name = args.get("object_name")
    input_bucket_name = args.get("container_name")
    notifiation_url = args.get("notifiation_url")
    output_bucket_name = args.get("output_bucket_name")

    api_url = args["api_url"]
    full_url = f'{args["base_url"]}{api_url}'
    timestamp = str(int(time.time() * 1000))

    sign_key = make_signature(
        api_url, timestamp, args["access_key"], args["secret_key"]
    )
    headers = make_header(timestamp, args["access_key"], sign_key)

    try:
        res = requests.put(full_url, headers=headers, data=json.dumps({
            'bucketName' : input_bucket_name,
            'pathList': [ object_name ],
            'notificationUrl': notifiation_url,
            'output': {
                'bucketName': 'phochak-shorts',
                'filePath': '/',
                'accessControl': 'PUBLIC_READ',
                'thumbnailAccessControl': 'PUBLIC_READ'
            }
        }))

        if res.status_code == 200:
            return {"input_bucket": input_bucket_name, "object_path": object_name, "res": res.text, "done": True}
        else:
            raise Exception({"done": False, "error_message": res.text})

    except Exception as e:
        raise Exception({"done": False, "error_message": str(e)})