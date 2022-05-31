delete
from shop.book_review_like
where user_id not in (1, 2);

delete
from shop.book_review_like
where review_id in (select id from shop.book_review r where r.user_id not in (1, 2));

delete
from shop.book_review
where user_id not in (1, 2);