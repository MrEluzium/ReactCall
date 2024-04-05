import onesignal
from onesignal.api import default_api
from onesignal.model.notification import Notification
import socketio

import tomllib
from pprint import pprint

with open("config.toml", mode="rb") as fp:
    config = tomllib.load(fp)

configuration = onesignal.Configuration(app_key=config['onesignal']['app_key'])
api_client = onesignal.ApiClient(configuration)
api_instance = default_api.DefaultApi(api_client)


def main():
    with socketio.SimpleClient() as sio:
        sio.connect(config['socket']['host'])
        print(f"Connected to {sio.client.connection_url}")
        while True:
            data = sio.receive()
            print(f"Received: {data}")
            if data[0] == 'START_CALL':
                notification = Notification(
                    app_id=config['onesignal']['app_id'],
                    included_segments=['Total Subscriptions'],
                    template_id='ababd3a2-bf2d-4cd9-aa6e-1948c93f9e96'
                )

                try:
                    api_response = api_instance.create_notification(notification)
                    pprint(api_response)
                except onesignal.ApiException as e:
                    print("Exception when calling DefaultApi->create_notification: %s\n" % e)


if __name__ == '__main__':
    main()
