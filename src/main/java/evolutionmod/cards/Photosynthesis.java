package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;
import evolutionmod.powers.GrowthPower;

import java.util.stream.Collectors;

public class Photosynthesis
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Photosynthesis";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantSkl.png";
    private static final int COST = -1;
    private static final int BRAMBLE_AMT = 2;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 2;

    public Photosynthesis() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = BRAMBLE_AMT;
//        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if (x > 0) {
            addToBot(new ApplyPowerAction(p, p, new BramblesPower(p, x * this.magicNumber)));
            int totalBlock = x * this.block;
            addToBot(new GainBlockAction(p, totalBlock));
//            addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, x)));
//            if (this.upgraded) {
//                addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, x)));
//            }
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!freeToPlayOnce) {
                    p.energy.use(energyOnUse);
                }
                this.isDone = true;
            }
        });
        boolean inForm = formEffect(PlantGene.ID);
        if (inForm) {
            adapt(1);
//            addToBot(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    this.isDone = true;
//                }
//            });
        }
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.getGeneId().equals(PlantGene.ID) ? 1 : 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initialRawDescription = UPGRADE_DESCRIPTION;
//            this.updateDescription();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(PlantGene.ID)) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}