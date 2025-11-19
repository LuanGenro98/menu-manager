"use client"

import { useState } from "react";
import { DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { DialogCategoryFormData, useDialogCategoryForm } from "./dialog-category-form"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";

interface DialogCategoryProps {
    closeModal: () => void;
    onRefresh: () => void;
    categoryId?: string;
    initialValues?: { name: string; description: string };
}

export function DialogCategory({ closeModal, initialValues, onRefresh, categoryId}: DialogCategoryProps){
    const form = useDialogCategoryForm({ initialValues: initialValues });
    const [loading, setLoading] = useState(false);
    
    async function onSubmit(values: DialogCategoryFormData){
        setLoading(true);

        const data = {
            "name": values.name,
            "description": values.description,
        }

        if(categoryId){
            const result = await fetch(`/api/categories/${categoryId}`, {
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

        const result = await fetch("/api/categories", {
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

    return (
        <>
            <DialogHeader>
                <DialogTitle>Nova Categoria</DialogTitle>
                <DialogDescription>
                    Adicionar nova categoria
                </DialogDescription>
            </DialogHeader>

            <Form {...form}>
                <form className="space-y-2" onSubmit={form.handleSubmit(onSubmit)}>
                    <div className="flex flex-col">
                        <FormField control={form.control} name="name" render={ ({ field }) => (
                            <FormItem className="my-2">
                                <FormLabel className="font-semibold">
                                    Nome da categoria:
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

                    <Button type="submit" className="w-full font-semibold text-white" disabled={loading}>
                        { loading ? "carregando..." : `${categoryId ? "Alterar categoria" : "Adicionar nova categoria"}`}
                    </Button>
                </form>
            </Form>
        </>
    )
}