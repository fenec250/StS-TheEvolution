package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.*;

import java.util.ArrayList;

public class TorisGift extends CustomRelic {
    public static final String ID = "evolutionmod:TorisGift";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/TorisGift.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/TorisGift_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int GENE_AMT = 1;

    public TorisGift() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
//    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
//        this.addToBot(new IncreaseMaxOrbAction(SLOT_AMT));

        ArrayList<String> genesPool = new ArrayList<>();
        genesPool.add(CentaurGene2.ID);
        genesPool.add(PlantGene2.ID);
        genesPool.add(MerfolkGene2.ID);
        genesPool.add(HarpyGene2.ID);
        genesPool.add(LavafolkGene2.ID);
        genesPool.add(SuccubusGene2.ID);
        genesPool.add(LymeanGene2.ID);
        genesPool.add(InsectGene2.ID);
        genesPool.add(BeastGene2.ID);
        genesPool.add(LizardGene2.ID);
        genesPool.add(ShadowGene2.ID);

        for (int i = 0; i < GENE_AMT; ++i) {
            String geneId = genesPool.get(AbstractDungeon.cardRandomRng.random(genesPool.size() - 1));
            AbstractDungeon.actionManager.addToBottom(BaseEvoCard.getGene(geneId).getChannelAction());
//            addToBot(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    gene.onStartOfTurn();
//                    this.isDone = true;
//                }
//            });
        }
        this.grayscale = true;
    }

    public void onVictory() {
        this.grayscale = false;
    }
}