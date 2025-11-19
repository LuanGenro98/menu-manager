"use client";

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Category } from "@/types/next-props";

export default function SelectCategory({ onRefresh }: { onRefresh: () => void }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [categories, setCategories] = useState<Category[]>([]);
  const selectedCategory = searchParams.get("category") ?? "all";

  useEffect(() => {
    async function loadCategories() {
      const res = await fetch("/api/categories");
      const data = await res.json();
      setCategories(data);
    }
    loadCategories();
  }, []);

  function handleSelectChange(value: string) {
    const params = new URLSearchParams(searchParams.toString());

    if (value === "all") {
      params.delete("category");
    } else {
      params.set("category", value);
    }

    router.push(`?${params.toString()}`);
  }

  useEffect(() => {
    onRefresh();
  }, [searchParams]);

  return (
    <>
    <div className="d-flex items-start">
      <label className="text-sm mb-2">
          Filtrar por:
      </label>
      <div className="w-42">
        <Select onValueChange={handleSelectChange} value={selectedCategory}>
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Selecione a categoria" />
          </SelectTrigger>

          <SelectContent className="w-full">
            <SelectItem value="all">Todas categorias</SelectItem>

            {categories.map(cat => (
              <SelectItem key={cat.id} value={String(cat.id)}>
                {cat.name}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

    </div>
    </>
  );
}
