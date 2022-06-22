# Simple Pet Clinic
A Simple Pet Clinic APP featuring OkHTTP and MVVM


## Main App

It presents a simple Pet Clinic App, featuring OKHttp, instead using most common libraries like retrofit. The app is divided into two modules: the main application and service.

### HttpService

Through the `ServiceModule` class, the module provides a pet clinic data repository for the user. The `PetClinicRepository` is an interface that contains the methods to access the data for both the app configuration and the list of pets. This interface is implemented by the `PetClinicClient`, which inherits from the `BaseClientBuilder`. 

The `BaseClientBuilder` parent is an abstract class that provides for its inheritors data such as the current URL and also an instance of the `OkHttpClient`. This OkHttp client is already set with the following interceptors: `HttpLoggingInterceptor` for debug purposes and the Retry interceptor, for cases when for some reason the request fails. At this moment, the retry chances are set on the class’ MAXIMUM_RETRIES in the companion object. The `BaseClientBuilder` also provides helper methods such as `buildGetRequest(path)` and a `doRequest(request)`, as this second one is able to parse the code and return the correct Api Response, containing an error or the correct data, build through a lambda passed to it in case of the request returns 200.

Based on the information above, the rough diagramam built for the `HttpService` module modeling is as follows:
  
  | ![space-1.jpg](https://user-images.githubusercontent.com/11355552/174922186-843ccd3f-ffc4-41e5-92b3-762a327b408f.png) | 
  |:--:| 
  | * UML Modeling for the HttpService Module * |



### Main App

The main application follows the MVVM architecture, based on a single activity and the usage of the `Android Jetpack Navigation` to navigate through the app’s screen. At the same case, it uses the new Splash Screen API presented on the Android 12 SDK.

Following a clean code architecture, the fragments are developed to be as dumb as possible, serving as a vehicle to the business rules and data processing presented on the view model. The view model, by its turn, communicate with the fragment and the screen through observed live data. 

The business rules and data processing is done on `UseCase` classes, that contain the correct implementation and can be invoked by the view model. Just like the view model, they too receive their dependencies through interfaces. This way, while they should process and access the data, they are unaware of the implementation of its dependencies. At this moment, the exception is the `IsOpenUseCase`, that can be refactored to have the Regex handling by a dependency, instead doing it directly by itself. Due to time constraints, it was left as is. 

The main idea behind the presented architecture is to consider a single flow of information, starting from basic interface function to denser business rules :

  | ![space-1.jpg](https://user-images.githubusercontent.com/11355552/174921989-bae99759-cdec-4cff-8162-f552b3a3813e.png) | 
  |:--:| 
  | * Followed architecture pattern * |
  
  

## Service Api

Considering the current simplicity of the API used by the application and due to the limited time and features on remote servers, it was decided to develop a simple service to provide the data for the app. It was developed using python, with the `Flask` library. The python application hosts a server on the current machine, providing the data contained in the JSON files on the root directory based on the selected path. 

Since the program uses a module, it may be necessary to install it first, using `pip`

```console
foo@bar~$ pip install flask
```

In some cases, python can be a bit nasty with modules and its environment, so it is a safe bet to install it using the following command on UNIX based OS:

```console
foo@bar~$ sudo apt-get install python3-pip
foo@bar~$ python3 -m pip install flask
```
As far as I know, it already exists on the `brew` version of python 

To host a server, simply access the `api-server` folder and run the following command:

```console
foo@bar~$ sudo python3 client.py
```

When successfully running, it should show the following data:
```console

* Debug mode: off
 * Running on all addresses (0.0.0.0)
   WARNING: This is a development server. Do not use it in a production deployment.
 * Running on http://127.0.0.1:5000
 * Running on http://192.168.0.9:5000 (Press CTRL+C to quit)
```

While the `127.0.0.1` address is a loopback, the other address should be able to be accessed through all hosts in the network, including android devices and emulators. To use it, simply update the `buildConfigField API_ADDRESS_URL` in the `HttpService` grade.module. To change the returning data, update the json files accordingly. 
 

