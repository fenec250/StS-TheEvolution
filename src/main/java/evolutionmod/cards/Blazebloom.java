package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.GrowthPower;

public class Blazebloom
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Blazebloom";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Firebloom.png";
    private static final int COST = 0;
    private static final int CHANNEL_AMT = 1;

    public Blazebloom() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = CHANNEL_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i) {
            addToBot(new LavafolkGene2().getChannelAction());
        }
//        formEffect(PlantGene.ID, () -> addToBot(new AbstractGameAction() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int g = (int)p.orbs.stream().filter(o -> LavafolkGene2.ID.equals(o.ID)).count();
                if (g > 0)
                    addToTop(new ApplyPowerAction(p, p, new GrowthPower(p, g)));
                this.isDone = true;
            }
        });
//      }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blazebloom();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}