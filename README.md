# Requirements

### Story 1 - As a web designer I would like to retrieve recipes from the back-end system, so I can display them in my app

Requirements:

- Without any additional query parameters, should return all recipes known to the back-end service
- Support filtering based on recipe category
- Support search strings, with the service then trying to match these in relevant fields (for example name and category)

### Story 2 - As a web designer I would like to retrieve the available recipe categories, so I can do more focused requests for specific recipe types

Requirements:

- Operation returns all recipe categories

### Story 3 - As a web designer I want to be able to add new recipes, so I can expand the recipe database with new, tasty and inspiring recipes

Requirements:

- When given valid input, creates a new recipe in the backend which can then be retrieved by the service's clients
- Make sure the provided input is valid
- Do not allow multiple recipes with the same name (so people don't get confused)

## Restrictions, requirements and expectations:

- You are free in the design of the REST interface. We will review your design on aspects such as paths, request and
  response bodies, HTTP response codes, etc.
- Use the Mendix Desktop Modeler for the Mendix portion of the assignment. The Web Modeler does not yet support
  consumption of REST services.
- While we do not expect any automated tests for the Mendix portion of the assignment, we do expect unit tests to cover
  the back-end functionality you built in stories 1-3.
- The recipes in the backend should be stored in memory. The recipes with which the app will initially launch are
  included as XML files with the assignment.

# Architecture:

## 1.0 Decisions

> As per email, we're using Json instead of XML for the initial recipes and since we're flexible with decisions;
> the following decisions were made

### 1.1 API decisions

* Converting all XMLs to JSON
* The JSON format would contain `{ "$type":"Recipe05", ....}` as type reference
* Using the `Amaretto_cake.json` schema as version v0.5.0
* Using `30_Minute_Chili.json` and `Another_Zucchini_Dish.json` schema as version v0.4.0
* v0.6.0 Changes
    * Because `ingredients` is a group of ingredients it will be a key:value map, this enforces group naming uniqueness and simplifies payload
        * key is the group name e.g Glaze
        * value is a group(aka array) of items each item has an amount (quantity, unit) and item.
        * Also For `ingredients` keys, we'll opt to using real names e.g `amount` instead of `amt` for more readability,
          and for i18n & l10n
    * For the `directions` property we'll make it an array of steps to preserve order, and for better displaying the steps instead of a bulk paragraph it can be a checkboxes of steps
* v0.5.0 Changes
    * ...
* v0.4.0 Changes
    * ...