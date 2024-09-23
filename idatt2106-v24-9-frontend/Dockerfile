FROM node:21.6.0

WORKDIR /app/frontend

COPY package.json .
COPY package-lock.json .

RUN npm install

COPY src ./src
COPY .env .
COPY .eslintrc.cjs .
COPY .prettierrc.json .
COPY cypress.config.ts .
COPY env.d.ts .
COPY index.html .
COPY postcss.config.js .
COPY tailwind.config.js .
COPY tsconfig.app.json .
COPY tsconfig.json .
COPY tsconfig.node.json .
COPY tsconfig.vitest.json .
COPY vite.config.ts .
COPY vitest.config.ts .

EXPOSE 5173

CMD ["npm", "run", "serve"]