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

export default function ItemsContent() {
  const searchParams = useSearchParams();
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<null>(null)
  const category = searchParams.get("category");

  async function handleDeleteItem(id: string){    
    const result = await fetch(`/api/items/${id}`, {
      method: "DELETE"
    });

    console.log(result);

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

      console.log("seta aqui:")
      console.log(data);

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
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
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
                            } : undefined} />
                        </DialogContent>
                </CardHeader>
                </Card>

                { items.map(item => (
                  <Card className="my-5" key={item.id}>
                    <CardContent>
                        <section className="space-y-2">
                                <article className="flex items-center justify-between">
                                    <div className="flex flex-col items-start gap-y-2 space-x-2">
                                        <div className="flex items-center space-x-2">
                                          <span className="font-semibold">{item.name}</span>
                                          <span className="text-gray-500">-</span>
                                          <span className="text-gray-400">{formatCurrency(item.price)}</span>
                                        </div>
                                        <div>
                                          <span>{item.description}</span>
                                        </div>
                                    </div>

                                    <div>
                                        <Button variant="ghost" size="icon" onClick={() => {handleEditItem(item)}}>
                                            <Pencil className="w-4 h-4"/>
                                        </Button>
                                        <Button variant="ghost" size="icon" onClick={() => {handleDeleteItem(item.id)}}>
                                            <X className="w-4 h-4"/>
                                        </Button>
                                    </div>
                                </article>
                        </section>
                    </CardContent>
              </Card>
            ))}
        </section>
    </Dialog>
    </>
)
}
