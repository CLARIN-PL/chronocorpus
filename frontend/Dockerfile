#FROM node:lts-alpine
#
## install simple http server for serving static content
#RUN npm install -g http-server
#
## make the 'app' folder the current working directory
#
#ENV DIRPATH /path
#
#WORKDIR $DIRPATH/$DIRNAME
#
## copy both 'package.json' and 'package-lock.json' (if available)
#COPY package*.json ./
#
## install project dependencies
#RUN npm install
#
## copy project files and folders to the current working directory (i.e. 'app' folder)
#COPY . .
#
## build app for production with minification
#RUN npm run build
#
#EXPOSE 8080
#
#CMD [ "http-server", "dist" ]

# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80

# Remove the default Nginx configuration file
RUN rm -v /etc/nginx/nginx.conf

# Copy a configuration file from the current directory
ADD nginx.conf /etc/nginx/

CMD ["nginx", "-g", "daemon off;"]
