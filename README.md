# Description
An application for executing algorithms written by HSE students in the course "Algorithms and Data Structures". The API is taken as an example https://crow-prod.ommat.ru , but to create the APK of the application, the URL is taken from env. There is also a github workflow for creating an APK artifact in which the URL is taken from a secret (see buildApk.yml).

The application uses MVVM architecture. Algorithmic tasks can be solved both via the API and offline, if the implementation has been preloaded to the device, but priority is given to the solution on the server.

For offline work, the Chaquopy library was used to work with python files.

# Screenshots
Main page:

![image](https://github.com/Simp-le/algorithms/assets/40547696/b76ce773-13f9-4370-816d-90b3276d8bd0)

Algorithm page:

![image](https://github.com/Simp-le/algorithms/assets/40547696/4230dc97-dfe8-48d9-b4a2-9c54bae0f317)

Offline main page:

![image](https://github.com/Simp-le/algorithms/assets/40547696/01d953cf-870e-493d-98ba-e349f445f326)

Offline algorithm page:

![image](https://github.com/Simp-le/algorithms/assets/40547696/cf55d6d9-4ef6-4a21-974c-4e95efe1872c)

