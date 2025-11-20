export interface Item {
    id: int,
    name: string,
    description: string,
    price: string;
    category_id: string;
    imageUrl?: string;
    hasOrders?: boolean;
}

export interface Category {
    id: number;
    name: string;
    description: string;
    items: Item[];
}
  
export interface Order {
    id?: number;
    itemsIds: number[];
    tableNumber: string;
    items: Item[];
    status: string;
    price: number;
  }
  

export interface OrderStatusProp {
    status: string;
}