"use client";

import { useState, useEffect } from "react"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
  } from "@/components/ui/dialog"

import {
    Card,
    CardAction,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Pencil, Plus, X } from "lucide-react"
import { formatCurrency } from "@/src/utils/formatCurrency"
import { DialogItem } from "./dialog-item"
import { toast } from "sonner"
import { Item } from "@/types/next-props";
import { convertAmericanToReal } from "@/src/utils/convertCurrency";
import SelectCategory from "./select-category";
import { useSearchParams } from "next/navigation";
import Image from "next/image";
import imageDefault from "@/public/menu-manager-background.jpg"

export default function ItemsContent() {
  const searchParams = useSearchParams();
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null)
  const category = searchParams.get("category");

  async function handleDeleteItem(id: string){    
    const result = await fetch(`/api/items/${id}`, {
      method: "DELETE"
    });

    if(!result.ok){
        toast.error("Falha ao remover item, tente novamente!");
        return;
    }

    setLoading(false);
    toast.success('Item removido com sucesso!');
    loadItems();
    return;
  }

  function handleEditItem(item: Item){
      setEditingItem(item);
      setIsDialogOpen(true);
  }

  async function loadItems() {
    try {
      const url = category ? `/api/items?category=${category}` : `/api/items`;

      const res = await fetch(url, { method: "GET" });

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
    loadItems();
  }, [])

  if (loading) return <p>Loading...</p>;

  return (
    <>
      <Dialog open={isDialogOpen} onOpenChange={(open) => {
        setIsDialogOpen(open)

        if (!open) {
          setEditingItem(null);
        }
      }
        }>
        <section className="mx-auto">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-xl md:text-2xl font-bold">Itens</CardTitle>
                        <div className="flex items-end gap-4">
                          <SelectCategory onRefresh={loadItems} />
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
                            <DialogItem closeModal={ () => {
                                setIsDialogOpen(false);
                                setEditingItem(null);
                            }} onRefresh={loadItems} itemId={editingItem ? editingItem.id : undefined} initialValues={editingItem ? {
                                name: editingItem.name,
                                description: editingItem.description,
                                price: convertAmericanToReal(editingItem.price),
                                category_id: String(editingItem.categoryId),
                                imageUrl: editingItem.imageUrl
                            } : undefined} />
                        </DialogContent>
                </CardHeader>
                </Card>

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 my-5 gap-5">
                  { items.map(item => (
                    <Card className="overflow-hidden rounded-xl border py-0 pb-2" key={item.id}>
                      <CardHeader className="p-0">
                      <Image
                        src={item.imageUrl ? item.imageUrl + "?t=" + Date.now()  : imageDefault}
                        width={300}
                        height={300}
                        className="w-full h-60 object-cover"
                        alt={item.name}
                        unoptimized={true}
                        priority
                      />
                      </CardHeader>

                      <CardContent className="px-4 py-2 space-y-2">
                        <div className="flex items-center justify-between">
                          <span className="text-lg text-muted-foreground">Nome:</span>
                          <span className="font-semibold">{item.name}</span>
                        </div>

                        <div className="flex items-center justify-between">
                          <span className="text-lg text-muted-foreground">Preco:</span>
                          <span className="font-semibold">{formatCurrency(item.price)}</span>
                        </div>

                        <div className="flex items-center">
                          <span className="text-lg text-muted-foreground">{item.description}</span>
                        </div>

                          <div>
                              <Button variant="outline" className="w-full text-md" onClick={() => {handleEditItem(item)}}>
                                  <Pencil className="w-6 h-6"/>                        
                                  Editar
                              </Button>
                              { !item.hasOrders && (
                                <Button variant="default" className="w-full text-md mt-2" onClick={() => {handleDeleteItem(item.id)}}>
                                  <X className="w-6 h-6"/>
                                  Remover
                                </Button>
                              )}
     
                          </div>
                    </CardContent>
                    </Card>
              ))}
            </div>
        </section>
    </Dialog>
    </>
)
}
