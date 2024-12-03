create table if not exists public.car_showrooms
(
    id
    bigserial
    primary
    key,
    address
    varchar
(
    255
) not null,
    name varchar
(
    255
) not null
    );

alter table public.car_showrooms
    owner to postgres;

create table if not exists public.categories
(
    id
    bigserial
    primary
    key,
    name
    varchar
(
    255
) not null
    );

alter table public.categories
    owner to postgres;

create table if not exists public.cars
(
    id
    bigserial
    primary
    key,
    brand
    varchar
(
    255
) not null,
    model varchar
(
    255
) not null,
    price double precision not null,
    year integer not null,
    category_id bigint
    constraint fk310labqcoawyj05t4gvvfjbg9
    references public.categories,
    showroom_id bigint
    constraint fkplld2nchgo4h82pyrd5snr5lq
    references public.car_showrooms
    );

alter table public.cars
    owner to postgres;

create table if not exists public.clients
(
    id
    bigserial
    primary
    key,
    name
    varchar
(
    255
) not null,
    registrationdate date not null
    );

alter table public.clients
    owner to postgres;

create table if not exists public.client_cars
(
    client_id
    bigint
    not
    null
    constraint
    fks4t8xib6loob12tsayv4oytbk
    references
    public
    .
    clients,
    car_id
    bigint
    not
    null
    constraint
    fkquh2q2r2g3f2ybdncggclnmyt
    references
    public
    .
    cars
);

alter table public.client_cars
    owner to postgres;

create table if not exists public.client_contacts
(
    client_id
    bigint
    not
    null
    constraint
    fk9bsjudxew45qcf8646idcv6f9
    references
    public
    .
    clients,
    contact_detail
    varchar
(
    255
),
    contact_type varchar
(
    255
) not null,
    primary key
(
    client_id,
    contact_type
)
    );

alter table public.client_contacts
    owner to postgres;

create table if not exists public.reviews
(
    id
    bigserial
    primary
    key,
    rating
    integer
    not
    null,
    text
    varchar
(
    1000
) not null,
    car_id bigint
    constraint fkieeb3p5v84i1xja7nbj1vkkeg
    references public.cars,
    client_id bigint
    constraint fko2cmyvyjrvumg4b3de9dcvfxa
    references public.clients
    );

alter table public.reviews
    owner to postgres;