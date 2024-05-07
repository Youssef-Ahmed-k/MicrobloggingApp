# First Stage
FROM node:current-alpine3.18 as build
RUN mkdir -p /app

WORKDIR /app

COPY package.json /app/
RUN npm install

COPY . /app/
RUN npm run build --prod

# Second Stage
FROM nginx:stable-alpine3.19
COPY --from=build /app/dist/frontend /usr/share/nginx/html