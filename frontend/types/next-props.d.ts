export interface Item {
    id?: int | undefined,
    name: string,
    description: string,
    price: string;
    category_id: string;
}

export interface Category {
    id?: int;
    name: string;
    description: string;
}

export interface Order {
    itemsIds: array;
    tableNumber: string;
}

export interface OrderStatus {
    status: string;
}