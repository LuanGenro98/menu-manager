"use client";

import { useState, useEffect } from "react"
import {
    Dialog,
    DialogContent,
    DialogTrigger,
  } from "@/components/ui/dialog"

import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import { formatCurrency } from "@/src/utils/formatCurrency"
import { DialogOrder } from "./dialog-order"
import Image from "next/image";
import { OrderStatus as OrderStatusComponent } from "./order-status";
import { useSearchParams } from "next/navigation";

import orderedImg from "@/public/menu-manager-ordered.png";
import preparingImg from "@/public/menu-manager-preparing.png";
import readyImg from "@/public/menu-manager-ready.png";
import servedImg from "@/public/menu-manager-served.png";
import paidImg from "@/public/menu-manager-paid.png";
import canceledImg from "@/public/menu-manager-canceled.png";
import SelectStatus from "./select-status";
import { Item, Order } from "@/types/next-props";

const statusImages: Record<string, any> = {
  ORDERED: orderedImg,
  PREPARING: preparingImg,
  READY: readyImg,
  SERVED: servedImg,
  PAID: paidImg,
  CANCELED: canceledImg,
};

export default function PedidosContent() {
  const searchParams = useSearchParams();
  const [items, setItems] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null)
  const status = searchParams.get("status");

  async function loadOrders() {
    try {
      const res = await fetch('/api/demands', { method: "GET" });

      if (!res.ok) throw new Error("Failed to fetch items");

      const data = await res.json();

      setItems(data);
    } catch (error) {
      console.error("Error fetching items:", error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadOrders();
  }, [])

  if (loading) return <p>Loading...</p>;

  return (
    <>
        <section className="mx-auto">
            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <Card>
                        <CardHeader className="flex flex-col md:flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-xl md:text-2xl font-bold">Pedidos</CardTitle>
                        <div className="flex flex-col items-center md:items-end gap-4 md:flex-row">
                          <SelectStatus onRefresh={loadOrders} />
                          <DialogTrigger asChild>
                              <Button>
                                  <Plus className="w-4 h-4"></Plus>
                              </Button>
                          </DialogTrigger>
                        </div>


                        <DialogContent onInteractOutside={(e) => {
                            e.preventDefault();
                            setIsDialogOpen(false);
                            setEditingItem(null);
                        }}>
                            <DialogOrder closeModal={ () => {
                                setIsDialogOpen(false);
                                setEditingItem(null);
                            }} onRefresh={loadOrders} orderId={editingItem ? editingItem.id : undefined} initialValues={editingItem ? {
                                tableNumber: editingItem.tableNumber,
                                itemsIds: editingItem.itemsIds,
                            } : undefined} />
                        </DialogContent>
                </CardHeader>
                </Card>
                </Dialog>

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 my-5 gap-5">
                { items.map(item => (
                  // item.status != "CANCELED" && item.status != "PAID" ? (
                    <Card className="overflow-hidden rounded-xl border py-0 pb-2" key={item.id}>
                    <CardHeader className="p-0">
                      <Image src={statusImages[item.status]} alt={item.status} className="h-60 object-cover self-end w-full" quality={100} priority/>
                    </CardHeader>

                    <CardContent className="px-4 py-2 space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="text-lg text-muted-foreground">Preço:</span>
                        <span className="font-semibold">{formatCurrency(item.price)}</span>
                      </div>

                      <div className="flex items-center justify-between">
                        <span className="text-lg text-muted-foreground">Mesa:</span>
                        <span className="font-semibold">{item.tableNumber}</span>
                      </div>

                      <OrderStatusComponent 
                        onRefresh={loadOrders} 
                        orderId={item.id} 
                        initialValues={{ status: item.status }} 
                      />

                    </CardContent>
                  </Card>
                //  ) : null

            ))}
          </div>
        </section>
    </>
)
}
