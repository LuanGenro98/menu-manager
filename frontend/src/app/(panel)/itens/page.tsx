import { Suspense } from "react"
import ItemsContent from "./_components/items-content"

export default function ItensPage() {
  return (
    <Suspense fallback={<p>Carregando...</p>}>
        <div className="mt-5">
          <ItemsContent />
        </div>
    </Suspense>
  )
}

