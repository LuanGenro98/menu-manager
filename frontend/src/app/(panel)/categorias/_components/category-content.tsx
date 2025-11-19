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
import { DialogCategory } from "./dialog-category"
import { toast } from "sonner"

interface Category {
  id: string;
  name: string;
  description: string;
}

export default function CategoryContent() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);

  async function handleDeleteItem(id: string){    
    const result = await fetch(`/api/categories/${id}`, {
      method: "DELETE"
    });

    if(!result.ok){
        toast.error("Falha ao remover categoria, tente novamente!");
        return;
    }

    setLoading(false);
    toast.success('categoria removido com sucesso!');
    loadCategories();
    return;
  }

  function handleEditItem(item: Category){
      setEditingCategory(item);
      setIsDialogOpen(true);
  }

  async function loadCategories() {
    try {
      const res = await fetch('/api/categories', { method: "GET" });

      if (!res.ok) throw new Error("Failed to fetch items");

      const data = await res.json();

      setCategories(data);
    } catch (error) {
      console.error("Error fetching items:", error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCategories();
  }, [])

  if (loading) return <p>Loading...</p>;

  return (
    <>
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <section className="mx-auto">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-xl md:text-2xl font-bold">Categorias</CardTitle>
                        <DialogTrigger asChild>
                              <Button>
                                  <Plus className="w-4 h-4"></Plus>
                              </Button>
                        </DialogTrigger>

                        <DialogContent onInteractOutside={(e) => {
                            e.preventDefault();
                            setIsDialogOpen(false);
                            setEditingCategory(null);
                        }}>
                            <DialogCategory 
                                closeModal={() => {
                                    setIsDialogOpen(false);
                                    setEditingCategory(null);
                                }} 
                                onRefresh={loadCategories} 
                                categoryId={editingCategory?.id} 
                                initialValues={editingCategory ? {
                                    name: editingCategory.name,
                                    description: editingCategory.description,
                                } : undefined} 
                            />
                        </DialogContent>
                </CardHeader>
                </Card>

                {categories.map(item => (
                  <Card className="my-5" key={item.id}>
                    <CardContent>
                        <section className="space-y-2">
                                <article className="flex items-center justify-between">
                                    <div className="flex flex-col items-start gap-y-2 space-x-2">
                                        <div className="flex items-center space-x-2">
                                          <span className="font-semibold">{item.name}</span>
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