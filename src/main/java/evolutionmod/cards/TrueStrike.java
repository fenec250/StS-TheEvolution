package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.orbs.LymeanGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.FatePower;

import java.util.List;
import java.util.stream.Collectors;

public class TrueStrike
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:TrueStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Strengthen.png";
    private static final int COST = 1;
    private static final int LYMEAN_FATE_AMT = 3;
    private static final int VIGOR_AMT = 2;
    private static final int MAX_VIGOR_AMT = 8;
    private static final int UPGRADE_MAX_VIGOR_AMT = 4;

    public TrueStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = MAX_VIGOR_AMT;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractCard> attacks = p.drawPile.group.stream()
                        .filter(c -> c.type == CardType.ATTACK)
                        .collect(Collectors.toList());
                addToTop(new ApplyPowerAction(p, p, new VigorPower(p, Math.min(attacks.size() * VIGOR_AMT, magicNumber))));
                attacks.forEach(c -> p.drawPile.moveToDiscardPile(c));
                this.isDone = true;
            }
        });
        if (formEffect(LymeanGene2.ID)) {
            addToBot(new ApplyPowerAction(p, p, new FatePower(p, LYMEAN_FATE_AMT)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TrueStrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_MAX_VIGOR_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (this.upgraded && isPlayerInThisForm(LymeanGene2.ID)) {
            this.glowColor = LymeanGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
