# ⌚ ListaDeTarefas - Gerenciamento de Áudio e Notificações (Wear OS)

Este projeto foi desenvolvido como parte dos requisitos da disciplina de [Nome da Disciplina] na [Nome da Faculdade]. O objetivo principal é demonstrar a implementação de um sistema robusto de feedback de voz (Text-to-Speech) em dispositivos Wear OS, priorizando a saída de áudio para fones de ouvido Bluetooth e tratando a detecção dinâmica de dispositivos.

## 🚀 Funcionalidades Principais

* **Text-to-Speech (TTS):** Implementação completa da API `TextToSpeech` para conversão de texto em fala.
* **Suporte a Idioma:** Configurado para utilizar o idioma Português (pt-BR).
* **Gerenciamento de Saída de Áudio (`AudioHelper`):** Classe auxiliar (`AudioHelper.kt`) para verificar a disponibilidade de dispositivos de áudio, essencial para ambientes Wear OS.
* **Priorização de Áudio:** Lógica para priorizar a reprodução via Bluetooth (A2DP), com *fallback* automático para o alto-falante embutido (Built-in Speaker) se o Bluetooth não estiver disponível.
* **Detecção Dinâmica:** Uso de `AudioDeviceCallback` para monitorar em tempo real a conexão e desconexão de fones de ouvido Bluetooth, alertando o usuário sobre a mudança no status de áudio.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Kotlin
* **Plataforma:** Android (especificamente Wear OS)
* **APIs:** `TextToSpeech`, `AudioManager`, `AudioDeviceCallback`, `AudioDeviceInfo`.

## 📂 Estrutura do Projeto

Os arquivos de código-fonte mais relevantes estão localizados em `app/src/main/java/com/example/listadetarefas/`:

| Arquivo | Descrição |
| :--- | :--- |
| `MainActivity.kt` | Contém a lógica principal (UI, `onCreate`), inicialização do TTS, registro do `AudioDeviceCallback`, e as funções de falar (`ensureAudioOutputAndSpeak`, `speakMessage`). |
| `AudioHelper.kt` | Classe auxiliar que encapsula a lógica de verificação de disponibilidade de saída de áudio (`audioOutputAvailable`). |
| `activity_main.xml` | Define o layout principal, incluindo o botão de ação que dispara o alerta de voz. |

## ⚙️ Como Executar

1.  **Pré-requisitos:** Android Studio com o SDK do Wear OS e um emulador ou dispositivo físico Wear OS.
2.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/pedr8k/ListaDeTarefas-WearOS.git](https://github.com/pedr8k/ListaDeTarefas-WearOS.git)
    ```
3.  **Abrir no Android Studio:** Abra a pasta clonada como um projeto Android existente.
4.  **Executar:** Selecione o emulador Wear OS (ex: *Wear OS Small Round*) e clique no botão **Run** (Executar).

Ao clicar no botão "Ação Principal" no aplicativo, o sistema reproduzirá a mensagem de alerta utilizando o melhor dispositivo de saída de áudio disponível.
