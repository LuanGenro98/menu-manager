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
import { DialogService } from "./dialog-service"
import { deleteService } from "../_actions/delete-service"
import { toast } from "sonner"

export default function ItemsContent() {
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingService, setEditingService] = useState<null>(null)

  useEffect(() => {
    (async () => {
      try {
        const res = await fetch("/api/items", { method: "GET" });

        console.log(res);

        if (!res.ok) throw new Error("Failed to fetch items");

        const data = await res.json();
        setItems(data);
      } catch (error) {
        console.error("Error fetching items:", error);
      } finally {
        setLoading(false);
      }
    })();
  }, [])

  if (loading) return <p>Loading...</p>;

  return (
    <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <section className="mx-auto">
            <Card>
                <CardContent>
                    <section className="space-y-2">
                        { items.map(item => (
                            <article key={item.id} className="flex items-center justify-between">
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
                                    <Button variant="ghost" size="icon" onClick={() => {}}>
                                        <Pencil className="w-4 h-4"/>
                                    </Button>
                                    <Button variant="ghost" size="icon" onClick={() => {}}>
                                        <X className="w-4 h-4"/>
                                    </Button>
                                </div>
                            </article>
                        ))}
                    </section>
                </CardContent>
            </Card>
        </section>
    </Dialog>

)
}
