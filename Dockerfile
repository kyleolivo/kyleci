FROM node:7.10.0

WORKDIR /app

RUN npm install -g nodemon
COPY . /app
RUN npm install && npm ls

CMD ["npm", "start"]

