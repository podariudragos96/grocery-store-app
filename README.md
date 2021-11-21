# Grocery Application

This is a simple console application that matches your input to a list of products and places them in your basket,
then when you're finished inserting the desired products, a receipt is printed for you, with some discounts applied,
depending on what you ordered.

Clarifying the chosen approach for some unclear requirements:

- For every 5 beverages (juice or coffe), you get a free beverage - always the beverages that are given for free are
the cheapest ones (e.g: you orde 1 small coffe, 6 medium coffes, 1 orange juice, 5 large coffes, you would have
2 free drinks on your order, in this case 1 small coffe, which is the cheapest, then 1 medium coffe, which is the
second cheapest. Also, free beverage discounts do not apply to beverage extras)

- Only coffes can have extras

- For every 1 snack and 1 beverage, a free extra is given. I chose the same approach here as for the free beverages
logic. Meaning, first of all, the cheapest extras are given for free, then the next cheapest and so on.