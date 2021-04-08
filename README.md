# Recipes:

## 1.0 Decisions

> As per email, we're using Json instead of XML for the initial recipes and since we're flexible with decisions;
> the following decisions were made

### 1.1 API decisions

* Converting all XMLs to JSON
* The JSON format would contain `{ "$type":"Recipe05", ....}` as type reference
* Using the `Amaretto_cake.json` schema as version v0.6.0
* Using `30_Minute_Chili.json` and `Another_Zucchini_Dish.json` schema as version v0.5.0
* v0.6.0 Changes
    * Because `ingredients` is a group of ingredients it will be a key:value map, this enforces group naming uniqueness
      and simplifies payload
        * key is the group name e.g Glaze
        * value will be an array of strings instead of complex `ingredient(item, amount (unit/qty)`:
            * Such complex object would make chefs struggle to write their recipes and be creative
            * The object is written already in string, because the original author knew such problem
            * This complexity introduced as a middle solution is unnecessary.
            * Cooking ingredient measurement is very complex to be listed, and it'd be better to use NLP or AI if certain
              analysis was needed in the future
    * For the `directions` property we'll make it an array of steps to preserve order, and for better displaying the
      steps instead of a bulk paragraph it can be a checkboxes of steps
* v0.5.0 Changes
    * ...
* v0.4.0 Changes
    * ...

## 2.0 Running the application

### 2.1 Run through docker

**Requirements**

1. Rename [.github/my_setting.xml](.github/my_settings.xml) to `settings.xml`
2. Go to your GitHub account https://github.com/settings/tokens/new
3. Create a token which has permission of `read:packages`
4. Copy that token to the [.github/setting.xml](.github/settings.xml) file in the password
5. Don't forget to put your github username instead of username

**Steps**

1. Make sure docker is installed
2. Inside the project main directory do `docker build . -t recipes:latest`
3. then `docker run -it recipes:latest`
4. Your application would be running on port 8090

### 2.2 Run on local

**Requirements**

1. Make sure you have openjdk 16
2. Maven 3.6.3
3. Make sure your `~/.m2/settings.xml` has access to GitHub packages (follow docker requirement steps)

**Steps**

1. Run `java --enable-preview -jar "/recipes/app/target/io.memoria.recipes-*.jar"`

## 3.0 TODOs

* Query result pagination
