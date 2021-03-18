package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;

import java.util.ArrayList;

public class TorisGift extends CustomRelic {
    public static final String ID = "evolutionmod:TorisGift";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/spellbook.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/spellbook_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int GENE_AMT = 1;
    private static final int SLOT_AMT = 1;
//    private static final int MAX_EXHAUST_AMT = 2;
//    private static final int UPGRADE_MAX_EXHAUST_AMT = 1;
//
    public TorisGift() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
//        this.addToBot(new IncreaseMaxOrbAction(SLOT_AMT));

        ArrayList<AbstractGene> genesPool = new ArrayList<>();
        genesPool.add(new CentaurGene());
        genesPool.add(new PlantGene());
        genesPool.add(new MerfolkGene());
        genesPool.add(new HarpyGene());
        genesPool.add(new LavafolkGene());
        genesPool.add(new SuccubusGene());
        genesPool.add(new LymeanGene());
        genesPool.add(new InsectGene());
        genesPool.add(new BeastGene());
        genesPool.add(new LizardGene());

        for (int i = 0; i < GENE_AMT; ++i) {
            AbstractGene gene = genesPool.get(AbstractDungeon.cardRng.random(genesPool.size() - 1));
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(gene.makeCopy()));
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    gene.onEvoke();
                    this.isDone = true;
                }
            });
        }
    }
}