FROM node:lts-alpine AS build

ARG VITE_APP_API_BASE_URL
ENV VITE_APP_API_BASE_URL=$VITE_APP_API_BASE_URL

WORKDIR /app

COPY . .

RUN npm install

RUN npm run build

FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
