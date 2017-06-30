
DROP TABLE IF EXISTS roomate_group;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS category;


-- -----------------------------------------------------
-- Table roomate_group
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS roomate_group (
  id INT(11) IDENTITY PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  date_created DATE NOT NULL
  )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
  id INT(11) IDENTITY PRIMARY KEY,
  group_id INT NULL,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  phone VARCHAR(50),
  password VARCHAR(150) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  FOREIGN KEY (group_id)
  REFERENCES roomate_group (id)
  )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table invite
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS invite (
  id INT(11) IDENTITY PRIMARY KEY,
  group_id INT NOT NULL,
  user_id INT NOT NULL,
  inviter_id INT,
  FOREIGN KEY (group_id)
  REFERENCES roomate_group (id),
  FOREIGN KEY (user_id)
  REFERENCES user (id),
  FOREIGN KEY (inviter_id)
  REFERENCES user (id)
  )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table user_role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_role (
  id INT(11) IDENTITY PRIMARY KEY,
  username VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL,
	FOREIGN KEY (username) REFERENCES user (username) 
  )
ENGINE = InnoDB;




-- -----------------------------------------------------
-- Table category
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS category (
  id INT(11) IDENTITY PRIMARY KEY,
  description VARCHAR(255) NOT NULL
    )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table bill
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS bill (
  id INT(11) IDENTITY PRIMARY KEY,
  user_id INT,
  group_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  price DECIMAL,
  date_created DATE NOT NULL,
  last_update_date DATE,
  description VARCHAR(255),
  category_id INT NOT NULL,
    FOREIGN KEY (user_id)
    REFERENCES user (id),
    FOREIGN KEY (group_id)
    REFERENCES roomate_group (id),
    FOREIGN KEY (category_id)
    REFERENCES category (id)
    )
ENGINE = InnoDB;


