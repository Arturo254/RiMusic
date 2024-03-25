package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ComposeLibraryAccessors laccForComposeLibraryAccessors = new ComposeLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KtorLibraryAccessors laccForKtorLibraryAccessors = new KtorLibraryAccessors(owner);
    private final RoomLibraryAccessors laccForRoomLibraryAccessors = new RoomLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for brotli (org.brotli:dec)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getBrotli() {
            return create("brotli");
    }

        /**
         * Creates a dependency provider for desugaring (com.android.tools:desugar_jdk_libs)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getDesugaring() {
            return create("desugaring");
    }

        /**
         * Creates a dependency provider for exoplayer (androidx.media3:media3-exoplayer)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getExoplayer() {
            return create("exoplayer");
    }

        /**
         * Creates a dependency provider for palette (androidx.palette:palette)
         * This dependency was declared in settings file 'settings.gradle.kts'
         */
        public Provider<MinimalExternalModuleDependency> getPalette() {
            return create("palette");
    }

    /**
     * Returns the group of libraries at compose
     */
    public ComposeLibraryAccessors getCompose() {
        return laccForComposeLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() {
        return laccForKotlinLibraryAccessors;
    }

    /**
     * Returns the group of libraries at ktor
     */
    public KtorLibraryAccessors getKtor() {
        return laccForKtorLibraryAccessors;
    }

    /**
     * Returns the group of libraries at room
     */
    public RoomLibraryAccessors getRoom() {
        return laccForRoomLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ComposeLibraryAccessors extends SubDependencyFactory {
        private final ComposeUiLibraryAccessors laccForComposeUiLibraryAccessors = new ComposeUiLibraryAccessors(owner);

        public ComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for activity (androidx.activity:activity-compose)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getActivity() {
                return create("compose.activity");
        }

            /**
             * Creates a dependency provider for coil (io.coil-kt:coil-compose)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getCoil() {
                return create("compose.coil");
        }

            /**
             * Creates a dependency provider for foundation (androidx.compose.foundation:foundation)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getFoundation() {
                return create("compose.foundation");
        }

            /**
             * Creates a dependency provider for ripple (androidx.compose.material:material-ripple)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getRipple() {
                return create("compose.ripple");
        }

            /**
             * Creates a dependency provider for shimmer (com.valentinilk.shimmer:compose-shimmer)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getShimmer() {
                return create("compose.shimmer");
        }

        /**
         * Returns the group of libraries at compose.ui
         */
        public ComposeUiLibraryAccessors getUi() {
            return laccForComposeUiLibraryAccessors;
        }

    }

    public static class ComposeUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public ComposeUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ui (androidx.compose.ui:ui)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> asProvider() {
                return create("compose.ui");
        }

            /**
             * Creates a dependency provider for util (androidx.compose.ui:ui-util)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getUtil() {
                return create("compose.ui.util");
        }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for coroutines (org.jetbrains.kotlinx:kotlinx-coroutines-core)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getCoroutines() {
                return create("kotlin.coroutines");
        }

    }

    public static class KtorLibraryAccessors extends SubDependencyFactory {
        private final KtorClientLibraryAccessors laccForKtorClientLibraryAccessors = new KtorClientLibraryAccessors(owner);
        private final KtorSerializationLibraryAccessors laccForKtorSerializationLibraryAccessors = new KtorSerializationLibraryAccessors(owner);

        public KtorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at ktor.client
         */
        public KtorClientLibraryAccessors getClient() {
            return laccForKtorClientLibraryAccessors;
        }

        /**
         * Returns the group of libraries at ktor.serialization
         */
        public KtorSerializationLibraryAccessors getSerialization() {
            return laccForKtorSerializationLibraryAccessors;
        }

    }

    public static class KtorClientLibraryAccessors extends SubDependencyFactory {
        private final KtorClientContentLibraryAccessors laccForKtorClientContentLibraryAccessors = new KtorClientContentLibraryAccessors(owner);

        public KtorClientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for cio (io.ktor:ktor-client-okhttp)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getCio() {
                return create("ktor.client.cio");
        }

            /**
             * Creates a dependency provider for core (io.ktor:ktor-client-core)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("ktor.client.core");
        }

            /**
             * Creates a dependency provider for encoding (io.ktor:ktor-client-encoding)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getEncoding() {
                return create("ktor.client.encoding");
        }

            /**
             * Creates a dependency provider for serialization (io.ktor:ktor-client-serialization)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getSerialization() {
                return create("ktor.client.serialization");
        }

        /**
         * Returns the group of libraries at ktor.client.content
         */
        public KtorClientContentLibraryAccessors getContent() {
            return laccForKtorClientContentLibraryAccessors;
        }

    }

    public static class KtorClientContentLibraryAccessors extends SubDependencyFactory {

        public KtorClientContentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for negotiation (io.ktor:ktor-client-content-negotiation)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getNegotiation() {
                return create("ktor.client.content.negotiation");
        }

    }

    public static class KtorSerializationLibraryAccessors extends SubDependencyFactory {

        public KtorSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for json (io.ktor:ktor-serialization-kotlinx-json)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getJson() {
                return create("ktor.serialization.json");
        }

    }

    public static class RoomLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public RoomLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for room (androidx.room:room-ktx)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> asProvider() {
                return create("room");
        }

            /**
             * Creates a dependency provider for compiler (androidx.room:room-compiler)
             * This dependency was declared in settings file 'settings.gradle.kts'
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() {
                return create("room.compiler");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComposeVersionAccessors vaccForComposeVersionAccessors = new ComposeVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: kotlin (1.9.22)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: ktor (2.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getKtor() { return getVersion("ktor"); }

            /**
             * Returns the version associated to this alias: media3 (1.2.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getMedia3() { return getVersion("media3"); }

            /**
             * Returns the version associated to this alias: room (2.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getRoom() { return getVersion("room"); }

        /**
         * Returns the group of versions at versions.compose
         */
        public ComposeVersionAccessors getCompose() {
            return vaccForComposeVersionAccessors;
        }

    }

    public static class ComposeVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public ComposeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: compose (1.6.1)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in settings file 'settings.gradle.kts'
         */
        public Provider<String> asProvider() { return getVersion("compose"); }

            /**
             * Returns the version associated to this alias: compose.compiler (1.5.8)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle.kts'
             */
            public Provider<String> getCompiler() { return getVersion("compose.compiler"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of plugins at plugins.kotlin
         */
        public KotlinPluginAccessors getKotlin() {
            return paccForKotlinPluginAccessors;
        }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlin.serialization to the plugin id 'org.jetbrains.kotlin.plugin.serialization'
             * This plugin was declared in settings file 'settings.gradle.kts'
             */
            public Provider<PluginDependency> getSerialization() { return createPlugin("kotlin.serialization"); }

    }

}
