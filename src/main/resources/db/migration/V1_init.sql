CREATE TABLE "users" (
  "id" SERIAL NOT NULL,
  "name" varchar(100) UNIQUE NOT NULL,
  "password" varchar(256) NOT NULL,
  "active" boolean DEFAULT true,
  "phone" varchar(15),
  "mail" varchar(100),
  "description" varchar(2048) NOT NULL,
  "age" integer NOT NULL,
  "city" varchar(100) NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "roles" (
  "id" SERIAL NOT NULL,
  "name" varchar(100) UNIQUE NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "users_roles" (
  "id" SERIAL NOT NULL,
  "user_id" integer NOT NULL,
  "role_id" integer NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "tags" (
  "id" SERIAL NOT NULL,
  "name" varchar(100) UNIQUE NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "users_tags" (
  "id" SERIAL NOT NULL,
  "user_id" integer NOT NULL,
  "tag_id" integer NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "files" (
  "id" SERIAL NOT NULL,
  "path" varchar(512) NOT NULL,
  "created_at" datetime,
  "user_id" integer NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE "matches" (
  "id" SERIAL NOT NULL,
  "sender_id" integer NOT NULL,
  "reciever_id" integer NOT NULL,
  "match" boolean NOT NULL,
  PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX ON "users_roles" ("user_id", "role_id");

CREATE UNIQUE INDEX ON "users_tags" ("user_id", "tag_id");

ALTER TABLE "users_roles" ADD CONSTRAINT "users_roles_fk0" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "users_roles" ADD CONSTRAINT "users_roles_fk1" FOREIGN KEY ("role_id") REFERENCES "roles" ("id");

ALTER TABLE "users_tags" ADD CONSTRAINT "users_tags_fk0" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "users_tags" ADD CONSTRAINT "users_tags_fk1" FOREIGN KEY ("tag_id") REFERENCES "tags" ("id");

ALTER TABLE "files" ADD CONSTRAINT "user_files_fk0" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "matches" ADD CONSTRAINT "sender_matches_fk0" FOREIGN KEY ("sender_id") REFERENCES "users" ("id");

ALTER TABLE "matches" ADD CONSTRAINT "reciever_matches_fk1" FOREIGN KEY ("reciever_id") REFERENCES "users" ("id");
