FROM node:lts-alpine as build-stage
RUN apk add --no-cache git
WORKDIR /app
RUN git clone https://github.com/CLARIN-PL/chronocorpus.git

WORKDIR /app/chronocorpus/frontend

RUN npm install
RUN npm run build

# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/chronocorpus/frontend/dist /usr/share/nginx/html

EXPOSE 80

# Remove the default Nginx configuration file
RUN rm -v /etc/nginx/nginx.conf

# Copy a configuration file from the current directory
ADD nginx.conf /etc/nginx/

CMD ["nginx", "-g", "daemon off;"]
