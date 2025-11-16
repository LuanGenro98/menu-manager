"use client"

import { useEffect, useState } from "react";
import { DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { DialogItemFormData, useDialogItemForm } from "./dialog-item-form"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { convertRealToAmerican, convertRealToCents } from "@/src/utils/convertCurrency";
import { toast } from "sonner";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Category, Item } from "@/types/next-props";

interface DialogItemProps {
    closeModal: () => void;
    onRefresh: () => void;
    itemId?: string;
    initialValues?: Item[]
}

export function DialogItem({ closeModal, initialValues, onRefresh, itemId}: DialogItemProps){
    console.log(initialValues);
    const [categories, setCategories] = useState<Category[]>([]);
    const form = useDialogItemForm({ initialValues: initialValues });
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        async function loadCategories() {
          const res = await fetch("/api/categories");
          const data = await res.json();
    
          setCategories(data);
    
          if (data.length === 1) {
            console.log(data);
            form.setValue("category_id", String(data[0].id), { shouldValidate: true });
          }
        }
    
        loadCategories();
    }, [form]);
    
    async function onSubmit(values: DialogItemFormData){
        setLoading(true);

        const data = {
            "name": values.name,
            "price": convertRealToAmerican(values.price),
            "description": values.description,
            "categoryId": Number(values.category_id)
        }

        if(itemId){
            const result = await fetch(`/api/items/${itemId}`, {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if(!result.ok){
                toast.error("Falha ao alterar item, tente novamente!");
                return;
            }

            setLoading(false);
            toast.success('Item alterado com sucesso!');
            onRefresh();
            handleCloseModal();
            return;
        }

        const result = await fetch("/api/items", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        setLoading(false);

        if (!result.ok) {
            toast.error("Falha ao adicionar item, tente novamente!");
            return;
          }
        
        toast.success('Item adicionado com sucesso!');
        onRefresh();
        handleCloseModal();
    }

    function handleCloseModal(){
        form.reset();
        closeModal();
    }

    function changeCurrency(event: React.ChangeEvent<HTMLInputElement>){
        let { value } = event.target;
        value = value.replace(/\D/g, '');

        if(value){
            value = (parseInt(value, 10) / 100).toFixed(2);
            value = value.replace('.', ',');
            value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        }

        event.target.value = value;
        form.setValue("price", value);
    }

    return (
        <>
            <DialogHeader>
                <DialogTitle>Novo Item</DialogTitle>
                <DialogDescription>
                    Adicionar novo Item
                </DialogDescription>
            </DialogHeader>

            <Form {...form}>
                <form className="space-y-2" onSubmit={form.handleSubmit(onSubmit)}>
                    <div className="flex flex-col">
                        <FormField control={form.control} name="name" render={ ({ field }) => (
                            <FormItem className="my-2">
                                <FormLabel className="font-semibold">
                                    Nome do item:
                                </FormLabel>
                                <FormControl>
                                    <Input {...field} placeholder="Digite o nome do item" />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}/>

                        <FormField control={form.control} name="description" render={ ({ field }) => (
                            <FormItem className="my-2">
                                <FormLabel className="font-semibold">
                                    Descrição
                                </FormLabel>
                                <FormControl>
                                    <Input {...field} placeholder="Digite a descrição"/>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}/>
                    </div>

                    <FormField control={form.control} name="price" render={ ({ field }) => (
                            <FormItem className="my-2">
                                <FormLabel className="font-semibold">
                                    Preço
                                </FormLabel>
                                <FormControl>
                                    <Input {...field} placeholder="Ex: 120,00" onChange={changeCurrency}/>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                    )}/>
                    <FormField
                        control={form.control}
                        name="category_id"
                        render={({ field }) => (
                            <FormItem className="my-2">
                            <FormLabel className="font-semibold">Categoria</FormLabel>

                            <FormControl>
                                <Select
                                value={field.value || ""}
                                onValueChange={field.onChange} 
                                >
                                <SelectTrigger>
                                    <SelectValue placeholder="Selecione a categoria" />
                                </SelectTrigger>

                                <SelectContent>
                                    {categories.map((cat) => (
                                    <SelectItem key={cat.id} value={String(cat.id)}>
                                        {cat.name}
                                    </SelectItem>
                                    ))}
                                </SelectContent>
                                </Select>
                            </FormControl>

                            <FormMessage />
                            </FormItem>
                        )}
                        />

                    <Button type="submit" className="w-full font-semibold text-white" disabled={loading}>
                        { loading ? "carregando..." : `${itemId ? "Alterar item" : "Adicionar novo item"}`}
                    </Button>
                </form>
            </Form>
        </>
    )
}